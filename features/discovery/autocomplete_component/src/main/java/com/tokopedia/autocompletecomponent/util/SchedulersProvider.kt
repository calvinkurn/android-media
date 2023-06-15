package com.tokopedia.autocompletecomponent.util

import rx.Scheduler

interface SchedulersProvider {

    fun io(): Scheduler
    fun ui(): Scheduler
    fun computation(): Scheduler
}
