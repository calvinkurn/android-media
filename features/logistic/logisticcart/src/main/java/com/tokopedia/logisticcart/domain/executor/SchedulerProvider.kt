package com.tokopedia.logisticcart.domain.executor

import rx.Scheduler

interface SchedulerProvider {
    fun io(): Scheduler
    fun ui(): Scheduler
}
