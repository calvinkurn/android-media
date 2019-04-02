package com.tokopedia.browse;

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.schedulers.Schedulers;

/**
 * @author by furqan on 06/09/18.
 */

public class RxAndroidTestPlugins {

    private static RxAndroidSchedulersHook androidRxHook = new RxAndroidSchedulersHook() {
        @Override
        public Scheduler getMainThreadScheduler() {
            return Schedulers.immediate();
        }
    };

    public static void resetAndroidTestPlugins() {
        RxAndroidPlugins.getInstance().reset();
    }

    public static void setImmediateScheduler() {
        RxAndroidPlugins.getInstance().reset();
        RxAndroidPlugins.getInstance().registerSchedulersHook(androidRxHook);
    }

}
