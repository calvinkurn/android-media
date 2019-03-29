package com.tokopedia.train.common.domain;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by nabillasabbaha on 30/08/18.
 */
public class TrainTestScheduler implements TrainProvider {

    public TrainTestScheduler() {
    }

    @Override
    public Scheduler computation() {
        return Schedulers.immediate();
    }

    @Override
    public Scheduler uiScheduler() {
        return Schedulers.immediate();
    }
}
