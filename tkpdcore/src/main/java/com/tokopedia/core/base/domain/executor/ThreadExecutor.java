package com.tokopedia.core.base.domain.executor;

import java.util.concurrent.Executor;

/**
 * will use Scheduler.io or Scheduler.newThread()
 */
@Deprecated
public interface ThreadExecutor extends Executor {

}
