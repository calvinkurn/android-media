package com.tokopedia.promocheckoutmarketplace

import androidx.annotation.VisibleForTesting
import androidx.test.espresso.idling.CountingIdlingResource

object PromoCheckoutIdlingResource {

    private const val RESOURCE_NAME = "PromoCheckout"

    private var idlingResource: CountingIdlingResource? = null

    fun increment() {
        idlingResource?.increment()
    }

    fun decrement() {
        idlingResource?.decrement()
    }

    @VisibleForTesting
    fun getIdlingResource(): CountingIdlingResource {
        idlingResource = CountingIdlingResource(RESOURCE_NAME)
        return idlingResource!!
    }
}