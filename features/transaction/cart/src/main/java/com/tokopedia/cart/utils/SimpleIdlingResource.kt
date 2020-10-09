package com.tokopedia.cart.utils

import androidx.test.espresso.idling.CountingIdlingResource

object SimpleIdlingResource {

    private const val RESOURCE = "CART"

    val countingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}