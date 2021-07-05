package com.tokopedia.search.utils

import rx.Scheduler

interface SchedulersProvider {

    fun io(): Scheduler
    fun ui(): Scheduler
    fun computation(): Scheduler
}