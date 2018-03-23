package com.tokopedia.sellerapp.runner;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.test.espresso.Espresso;

import com.tokopedia.sellerapp.util.RxIdlingExecutionHook;
import com.tokopedia.sellerapp.util.RxIdlingResource;

import rx.Scheduler;
import rx.functions.Func1;
import rx.plugins.RxJavaHooks;
import rx.plugins.RxJavaPlugins;
import rx.schedulers.Schedulers;

/**
 * Runner that registers a Espresso Indling resource that handles waiting for
 * RxJava Observables to finish.
 * WARNING - Using this runner will block the tests if the application uses long-lived hot
 * Observables such us event buses, etc.
 */
public class RxAndroidJUnitRunner extends UnlockDeviceAndroidJUnitRunner {

    @Override
    public void onCreate(Bundle arguments) {
        super.onCreate(arguments);
        RxIdlingResource rxIdlingResource = new RxIdlingResource();
        RxJavaHooks.setOnIOScheduler(new Func1<Scheduler, Scheduler>() {
            @Override
            public Scheduler call(Scheduler scheduler) {
                return Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        RxJavaHooks.setOnNewThreadScheduler(new Func1<Scheduler, Scheduler>() {
            @Override
            public Scheduler call(Scheduler scheduler) {
                return Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        RxJavaHooks.setOnNewThreadScheduler(new Func1<Scheduler, Scheduler>() {
            @Override
            public Scheduler call(Scheduler scheduler) {
                return Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        RxJavaPlugins.getInstance()
                .registerObservableExecutionHook(new RxIdlingExecutionHook(rxIdlingResource));
        Espresso.registerIdlingResources(rxIdlingResource);
    }
}
