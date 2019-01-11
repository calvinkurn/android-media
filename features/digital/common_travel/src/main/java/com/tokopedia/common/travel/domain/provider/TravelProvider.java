package com.tokopedia.common.travel.domain.provider;

import rx.Scheduler;

/**
 * Created by nabillasabbaha on 19/11/18.
 */
public interface TravelProvider {

    Scheduler computation();

    Scheduler uiScheduler();
}
