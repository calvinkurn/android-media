package com.tokopedia.logisticaddaddress.domain.executor

import rx.Scheduler

interface SchedulerProvider {
    fun io(): Scheduler
    fun ui(): Scheduler
}
