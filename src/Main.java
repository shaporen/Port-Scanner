import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.*;

public class Main {

    public static final int THREADS = 100;
    public static final int TIMEOUT = 100;
    public static final int MIN_PORT_NUM = 0;
    public static final int MAX_PORT_NUM = 65535;

    public static void main(String[] args) {
        scan("online-timer.ru");
    }

    private static void scan(String host) {
        System.out.println("Scanning ports: ");
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);

        for (int i = MIN_PORT_NUM; i < MAX_PORT_NUM; i++) {
            final int port = i;
            executor.execute(() -> {
                var inetSocketAdress = new InetSocketAddress(host, port);
                try (var socket = new Socket()) {
                    socket.connect(inetSocketAdress, TIMEOUT);
                    System.out.printf("Host: %s, port %s is opened\n", host, port);
                } catch (IOException e) {
//                    System.err.println(e.getMessage());;
                }
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            new RuntimeException(e);
        }
        System.out.println("Finish!");
    }
}