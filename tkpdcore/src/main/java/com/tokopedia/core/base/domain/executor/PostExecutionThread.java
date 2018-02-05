package com.tokopedia.core.base.domain.executor;

import rx.Scheduler;

/**
 * will use AndroidSchedulers.mainThread()
 */
@Deprecated
public interface PostExecutionThread {
    Scheduler getScheduler();
}
