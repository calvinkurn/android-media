package com.tokopedia.train.common.domain;

import rx.Scheduler;

/**
 * Created by nabillasabbaha on 30/08/18.
 */
public interface TrainProvider {

    Scheduler computation();

    Scheduler uiScheduler();
}
