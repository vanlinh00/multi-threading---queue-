import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// Đối tượng mô tả đơn hàng
class Order {
    private String orderId;
    public Order(String orderId) {
        this.orderId = orderId;
    }
    public String getOrderId() {
        return orderId;
    }
}

// Hệ thống xử lý đơn hàng
public class Main {

    private final BlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>();

    // ThreadPool với 3 worker xử lý các tác vụ song song
    private final ExecutorService executorService = Executors.newFixedThreadPool(3);

    // Hàm để thêm đơn hàng vào queue
    public void placeOrder(Order order) {
        try {
            orderQueue.put(order);
            System.out.println("Placed order: " + order.getOrderId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Worker xử lý đơn hàng từ queue
    public void processOrders() {
        while (true) {
            try {
                // Lấy đơn hàng từ queue
                Order order = orderQueue.take();
                System.out.println("Processing order: " + order.getOrderId());

                // Tạo các tác vụ khác nhau xử lý song song
                executorService.submit(() -> checkInventory(order));
                executorService.submit(() -> processPayment(order));
                executorService.submit(() -> saveOrder(order));
                executorService.submit(() -> sendConfirmationEmail(order));

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Giả lập kiểm tra hàng tồn kho
    private void checkInventory(Order order) {
        try {
            System.out.println("Checking inventory for order: " + order.getOrderId());
            TimeUnit.SECONDS.sleep(1);  // Giả lập thời gian xử lý
            System.out.println("Inventory check completed for order: " + order.getOrderId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Giả lập xử lý thanh toán
    private void processPayment(Order order) {
        try {
            System.out.println("Processing payment for order: " + order.getOrderId());
            TimeUnit.SECONDS.sleep(2);  // Giả lập thời gian xử lý
            System.out.println("Payment processed for order: " + order.getOrderId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Giả lập lưu đơn hàng
    private void saveOrder(Order order) {
        try {
            System.out.println("Saving order: " + order.getOrderId());
            TimeUnit.SECONDS.sleep(1);  // Giả lập thời gian xử lý
            System.out.println("Order saved: " + order.getOrderId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Giả lập gửi email xác nhận
    private void sendConfirmationEmail(Order order) {
        try {
            System.out.println("Sending confirmation email for order: " + order.getOrderId());
            TimeUnit.SECONDS.sleep(1);  // Giả lập thời gian xử lý
            System.out.println("Confirmation email sent for order: " + order.getOrderId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        Main system = new Main();

        // Bắt đầu thread để xử lý các đơn hàng trong queue
        new Thread(system::processOrders).start();

        // Giả lập các đơn hàng đến từ khách hàng
        for (int i = 1; i <= 1; i++) {
            String orderId = "Order #" + i;
            system.placeOrder(new Order(orderId));
        }
    }
}
