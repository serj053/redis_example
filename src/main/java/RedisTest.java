import org.redisson.Redisson;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.Date;
import java.util.Iterator;
import java.util.Random;

public class RedisTest {
    public static void main(String[] args) throws InterruptedException {

        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        RedissonClient redisson = Redisson.create(config);
        RScoredSortedSet<String> sortedSet = redisson.getScoredSortedSet("users");

        for (int i = 0; i < 20; i++) {
            sortedSet.add(new Date().getTime(), String.valueOf(i + 1));
        }

        Random random = new Random();
        int user_insert_point = random.nextInt(1, 11);
        boolean flag = true;
        Iterator<String> element = sortedSet.iterator();
        while (element.hasNext()) {
            String elem = sortedSet.pollFirst();
            if (flag && user_insert_point == Integer.parseInt(elem)) {
                int user_paid = random.nextInt(1, 21);
                System.out.println("блатной пользователь " + user_paid + "  оплатил услугу");
                sortedSet.add(new Date().getTime(), String.valueOf(user_paid));
                user_insert_point = random.nextInt(10, 20);
                flag = false;
            } else if (!flag && user_insert_point == Integer.parseInt(elem)) {
                int user_paid = random.nextInt(1, 21);
                System.out.println("блатной пользователь " + user_paid + "  оплатил услугу");
                sortedSet.add(new Date().getTime(), String.valueOf(user_paid));
                user_insert_point = random.nextInt(1, 11);
                flag = true;
            }
            System.out.println(" На главной странице показываем пользователя  " + elem);
            sortedSet.add(new Date().getTime(), elem);
            Thread.sleep(1000);
        }
        sortedSet.removeRangeByRank(0, -1);

    }
}
