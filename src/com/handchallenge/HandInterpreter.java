package com.handchallenge;

import java.io.*;

public class HandInterpreter {
    private final String input;
    private int currentSize = 1024;
    private int[] memory = new int[currentSize];
    private int instructionPointer = 0;
    private int memoryPointer = 0;

    private final static String POINT_RIGHT = "👉";
    private final static String POINT_LEFT = "👈";
    private final static String POINT_UP = "👆";
    private final static String POINT_DOWN = "👇";
    private final static String FIST_RIGHT = "🤜";
    private final static String FIST_LEFT = "🤛";
    private final static String FIRST_FRONT = "👊";

    public static void main(String[] args) throws IOException {
        HandInterpreter interpreter = new HandInterpreter(args);
        interpreter.run();
    }

    public HandInterpreter(String[] args) throws IOException {
        input = getInput(args);
    }

    private void run() {
        while (currentIndex() < input.length()) {
            switch (currentCommand()) {
                case POINT_RIGHT:
                    increaseMemoryPointer();
                    break;
                case POINT_LEFT:
                    decreaseMemoryPointer();
                    break;
                case POINT_UP:
                    increaseMemoryValue();
                    break;
                case POINT_DOWN:
                    decreaseMemoryValue();
                    break;
                case FIST_RIGHT:
                    jumpIfEqual();
                    break;
                case FIST_LEFT:
                    jumpIfNotEqual();
                    break;
                case FIRST_FRONT:
                    printCurrentMemory();
                    break;
                default:
                    throw new RuntimeException("unexpected command received");
            }
        }
    }


    public int currentIndex() {
        return instructionPointer * 2; // due to java's handling of emojis
    }

    public void increaseInstructionPointer() {
        instructionPointer++;
    }
    public void decreaseInstructionPointer() {
        instructionPointer--;
    }
    public String currentCommand() {
        return String.valueOf(Character.toChars(input.codePointAt(currentIndex())));
    }

    public void decreaseMemoryValue() {
        if (memory[memoryPointer] == 0) {
            memory[memoryPointer] = 255;
        } else {
            memory[memoryPointer]--;
        }
        increaseInstructionPointer();
    }

    public void increaseMemoryValue() {
        memory[memoryPointer] = (memory[memoryPointer] + 1) % 256;
        increaseInstructionPointer();
    }

    public void decreaseMemoryPointer() {
        memoryPointer--;
        if (memoryPointer < 0) {
            throw new RuntimeException("attempting to access wrong memory position. Current idx: " + instructionPointer);
        }
        increaseInstructionPointer();
    }

    public void increaseMemoryPointer() {
        memoryPointer++;
        increaseInstructionPointer();
    }

    public void printCurrentMemory() {
        System.out.print(Character.toChars(memory[memoryPointer]));
        increaseInstructionPointer();
    }

    public void jumpIfEqual() {
        if (memory[memoryPointer] == 0) {
            int pendingRightFistCounter = 1;
            while(pendingRightFistCounter != 0) {
                increaseInstructionPointer();
                if (currentCommand().equals(FIST_LEFT)) {
                    pendingRightFistCounter--;
                } else if (currentCommand().equals(FIST_RIGHT)) {
                    pendingRightFistCounter++;
                }
            }
        }
        increaseInstructionPointer();

    }

    public void jumpIfNotEqual() {
        if (memory[memoryPointer] != 0) {
            int pendingLeftFistCounter = 1;
            while(pendingLeftFistCounter != 0) {
                decreaseInstructionPointer();
                if (currentCommand().equals(FIST_RIGHT)) {
                    pendingLeftFistCounter--;
                } else if (currentCommand().equals(FIST_LEFT)) {
                    pendingLeftFistCounter++;
                }
            }
        }
        increaseInstructionPointer();
    }

    private String getInput(String[] args) throws IOException {
        final String inputFile;
        if (args.length > 1) {
            inputFile = args[1];
        } else {
            inputFile = "input.hand";
        }
        BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)));
        return br.readLine();
    }
}
