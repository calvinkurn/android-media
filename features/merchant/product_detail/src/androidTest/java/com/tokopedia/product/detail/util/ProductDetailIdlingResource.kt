package com.tokopedia.product.detail.util

import androidx.test.espresso.idling.CountingIdlingResource

object ProductDetailIdlingResource {
    private const val RESOURCE = "GLOBAL_PRODUCT_DETAIL"

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