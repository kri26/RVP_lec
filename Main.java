import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {
    public static void main(String[] args)
    {
        Bank bank0 = new Bank(500, 5, 0);
        Bank bank1 = new Bank(500, 5, 1);
        Bank bank2 = new Bank(500, 5, 2);

        bank0.start();
        bank1.start();
        bank2.start();

        try {
            bank0.join();
            bank1.join();
            bank2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Bank extends Thread{
    static List<Bank> banks = new CopyOnWriteArrayList<Bank>();
    private final Object lock = new Object();
    private int t = 0;

    private int sum;
    private int count;
    private int id;

    public Bank(int sum, int count, int id) {
        super();

        this.sum = sum;
        this.count = count;
        this.id = id;

        banks.add(this);
    }

    @Override
    public void run() {
        for (int i = 0; i < count; i++) {
            Bank bank = this;

            while (bank == this)
                bank = banks.get((int)(Math.random() * banks.size()));

            int money = (int)(Math.random() * 90 + 10);

            synchronized (lock) {
                t += 1;
                sum -= money;
            }

            System.out.println("Банк № " + id + " отправил " + money + " в " + t + ", сумма " + sum);

            bank.send(money, t);

            try {
                sleep((int) (Math.random() * 1000));
            } catch (InterruptedException e) {  }

        }
    }

    private void send(int money, int t) {
        synchronized (lock) {
            this.sum += money;
            this.t = Math.max(this.t, t) + 1;
        }

        System.out.println("Банк № " + id + " получил " + money + " в " + t + ", сумма " + sum);
    }
}
