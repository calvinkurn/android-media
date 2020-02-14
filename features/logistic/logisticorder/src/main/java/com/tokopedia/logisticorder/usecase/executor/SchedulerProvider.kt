package com.tokopedia.logisticorder.usecase.executor

import rx.Scheduler

interface SchedulerProvider {
    fun io(): Scheduler
    fun ui(): Scheduler
}