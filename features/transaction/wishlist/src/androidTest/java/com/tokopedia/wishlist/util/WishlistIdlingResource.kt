package com.tokopedia.wishlist.util

import androidx.test.espresso.idling.CountingIdlingResource

object WishlistIdlingResource {

    private const val RESOURCE_NAME = "Wishlist"

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