package com.tokopedia.pms.analytics

import androidx.test.espresso.idling.CountingIdlingResource

object PmsIdlingResource {
    private const val RESOURCE = "GLOBAL_PAYMENT_LIST"

    @JvmField
    val idlingResource: CountingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        idlingResource.increment()
    }

    fun decrement() {
        if(!idlingResource.isIdleNow) {
            idlingResource.decrement()
        }
    }
}