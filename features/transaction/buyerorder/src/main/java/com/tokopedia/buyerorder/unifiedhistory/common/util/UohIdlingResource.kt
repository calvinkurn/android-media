package com.tokopedia.buyerorder.unifiedhistory.common.util

import androidx.test.espresso.idling.CountingIdlingResource

/**
 * Created by fwidjaja on 07/11/20.
 */
object UohIdlingResource {

    private const val RESOURCE_NAME = "UOH"

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