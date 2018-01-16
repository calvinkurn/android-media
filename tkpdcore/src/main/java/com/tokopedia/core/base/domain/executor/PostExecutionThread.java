package com.tokopedia.core.base.domain.executor;

import rx.Scheduler;

@Deprecated
public interface PostExecutionThread {
    Scheduler getScheduler();
}
