package com.tokopedia.train.common.domain;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by nabillasabbaha on 30/08/18.
 */
public class TrainScheduler implements TrainProvider {

    public TrainScheduler() {
    }

    @Override
    public Scheduler computation() {
        return Schedulers.io();
    }

    @Override
    public Scheduler uiScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
