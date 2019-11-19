package com.tokopedia.user_identification_common.util;

import rx.Scheduler;
import rx.schedulers.Schedulers;

public class TestSchedulerProvider implements SchedulerProvider {

    @Override
    public Scheduler ui() {
        return Schedulers.immediate();
    }

    @Override
    public Scheduler io() {
        return Schedulers.immediate();
    }

}
