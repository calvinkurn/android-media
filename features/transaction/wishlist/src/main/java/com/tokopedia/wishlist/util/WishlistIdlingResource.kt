package com.tokopedia.wishlist.util

import androidx.test.espresso.idling.CountingIdlingResource

object WishlistIdlingResource {

    private const val RESOURCE_NAME = "WishlistIdlingResource"

    val countingIdlingResource = CountingIdlingResource(RESOURCE_NAME)

    fun increment() {
        if (countingIdlingResource.isIdleNow) {
            countingIdlingResource.increment()
        }
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}
