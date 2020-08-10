package com.tokopedia.sellerhome.common

import androidx.test.espresso.idling.CountingIdlingResource

object SellerHomeIdlingResource {
    private const val RESOURCE = "GLOBAL_SELLER_HOME"

    @JvmField
    val idlingResource: CountingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        idlingResource.increment()
    }

    fun decrement() {
        if (!idlingResource.isIdleNow) {
            idlingResource.decrement()
        }
    }
}