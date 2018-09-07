package com.tokopedia.core.base.domain.executor;

import rx.Scheduler;

/**
 * Use Usecase from tkpd usecase
 */
@Deprecated
public interface PostExecutionThread {
    Scheduler getScheduler();
}
