package lab3;

import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class matrix_2 {
    private final double[][] firstmatrix;
    private final double[][] secondmatrix;
    private final double[][] result;

    public matrix_2(double[][] matrix, double[][] vector) {
        this.firstmatrix = matrix;
        this.secondmatrix = vector;
        this.result = new double[firstmatrix.length][secondmatrix[0].length];
    }

    private class MultiplicationTask implements Runnable {
        private final int startRow;
        private final int endRow;

        public MultiplicationTask(int startRow, int endRow) {
            this.startRow = startRow;
            this.endRow = endRow;
        }

        @Override
        public void run() {  // умножение двух матриц

            for (var i = startRow; i < endRow; i++) {
                for (var j = 0; j < result[0].length; j++) {
                    result[i][j] = 0;
                    for (var k = 0; k < firstmatrix[0].length; k++) {
                        result[i][j] += firstmatrix[i][k] * secondmatrix[k][j];
                    }
                }
            }
        }
    }

    public double[][] multiply() {
        long time = System.nanoTime();
        int numThreads = 3;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        int rowsPerThread = firstmatrix.length / numThreads;
        int remainingRows = firstmatrix.length % numThreads;
        for (int i = 0; i < numThreads; i++) {
            int startRow = i * rowsPerThread;
            int endRow = (i == numThreads - 1) ? firstmatrix.length : startRow + rowsPerThread;
            executor.execute(new MultiplicationTask(startRow, endRow));
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Затратилось времени: " + ((double)(System.nanoTime() - time)) + " наносекунд");
        return result;
    }

    public static void main(final String[] args)
    {
        final var scanner = new Scanner(System.in);
        double[][] matr1 = scanMatrix(scanner);
        double[][] vector1 = scanMatrix(scanner);
        matrix_2 multiplication = new matrix_2(matr1, vector1);
        long time = System.currentTimeMillis();
        double[][] result1 = multiplication.multiply();
        System.out.println();
        System.out.println(Arrays.deepToString(result1));
    }
    public static double[][] scanMatrix(Scanner scanner) {
        System.out.println("Введите количество строк матрицы:");
        final var rows = scanner.nextInt();
        System.out.println("Введите количество столбцов матрицы:");
        final var cols = scanner.nextInt();
        final var matrixA = new double[rows][cols];
        System.out.println("Введите матрицу:");
        for (var i = 0; i < rows; i++) {
            for (var j = 0; j < cols; j++) {
                matrixA[i][j] = scanner.nextDouble();
            }
        }
        return matrixA;
    }
    /*public static double[] scanVector(Scanner scanner) {
        System.out.println("Введите количество элементов вектора:");
        final var elem = scanner.nextInt();
        final var matrixA = new double[elem];
        System.out.println("Введите матрицу:");
        for (var i = 0; i < elem; i++) {
            matrixA[i] = scanner.nextDouble();
        }
        return matrixA;
    }*/
}