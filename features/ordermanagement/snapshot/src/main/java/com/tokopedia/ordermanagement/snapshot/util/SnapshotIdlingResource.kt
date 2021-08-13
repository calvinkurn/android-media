package com.tokopedia.ordermanagement.snapshot.util

import androidx.test.espresso.idling.CountingIdlingResource

/**
 * Created by fwidjaja on 2/6/21.
 */
object SnapshotIdlingResource {
    private const val RESOURCE_NAME = "SNAPSHOT"

    @JvmField val countingIdlingResource = CountingIdlingResource(RESOURCE_NAME)

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}