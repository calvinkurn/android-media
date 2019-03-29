package com.tokopedia.common.travel.domain.provider;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by nabillasabbaha on 19/11/18.
 */
public class TravelScheduler implements TravelProvider {

    public TravelScheduler() {
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
