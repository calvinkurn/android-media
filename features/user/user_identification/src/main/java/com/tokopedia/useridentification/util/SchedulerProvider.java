package com.tokopedia.useridentification.util;

import rx.Scheduler;

public interface SchedulerProvider {
    Scheduler ui();
    Scheduler io();
}
