package com.cycloneboy.se.thread;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

/**
 * Phaser 同步屏障
 * Created by CycloneBoy on 2017/9/7.
 */
public class PhaserDemo {
    public static void main(String[] args) {
        List<Runnable> tasks = new ArrayList<>();
        tasks.add(()->System.out.printf("%s running at %d%n",
                Thread.currentThread().getName(),
                System.currentTimeMillis()));

        tasks.add(()-> System.out.printf("%s running at %d%n",
                Thread.currentThread().getName(),
                System.currentTimeMillis()));

        runTasks(tasks);
    }

    private static void runTasks(List<Runnable> tasks) {
        final Phaser phaser = new Phaser(1); // "1" （register self)
        // create and start threads
        for(final Runnable task: tasks){
            phaser.register();
            Runnable r = ()->{
                try{
                    Thread.sleep(50 + (int)(Math.random() * 300));
                }catch (InterruptedException ie){
                    System.out.println("interrupt thread");
                }

                phaser.arriveAndAwaitAdvance();// await the ... creation of all tasks
                task.run();
            };
            Executors.newSingleThreadExecutor().execute(r);
        }

        // allow threads to strat and deregister self
        phaser.arriveAndDeregister();
    }


}