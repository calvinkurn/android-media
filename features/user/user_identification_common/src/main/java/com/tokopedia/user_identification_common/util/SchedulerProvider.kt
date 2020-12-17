package com.tokopedia.user_identification_common.util

import rx.Scheduler

interface SchedulerProvider {
    fun ui(): Scheduler?
    fun io(): Scheduler?
}