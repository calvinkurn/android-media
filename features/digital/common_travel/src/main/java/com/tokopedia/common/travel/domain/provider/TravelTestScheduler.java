package com.tokopedia.common.travel.domain.provider;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by nabillasabbaha on 19/11/18.
 */
public class TravelTestScheduler implements TravelProvider {

    public TravelTestScheduler() {
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
