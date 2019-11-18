package com.tokopedia.user_identification_common.util;

import rx.Scheduler;

public interface SchedulerProvider {
    Scheduler ui();
    Scheduler io();
}
