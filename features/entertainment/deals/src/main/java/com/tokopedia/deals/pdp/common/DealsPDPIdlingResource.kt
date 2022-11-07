package com.tokopedia.deals.pdp.common

import androidx.annotation.VisibleForTesting
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource

object DealsPDPIdlingResource {
    private const val RESOURCE_NAME = "DEALS_PDP"

    private var idlingResource: CountingIdlingResource? = null

    fun increment() {
        idlingResource?.increment()
    }

    fun decrement() {
        idlingResource?.decrement()
    }

    @VisibleForTesting
    fun getIdlingResource(): IdlingResource? {
        idlingResource = CountingIdlingResource(RESOURCE_NAME)
        return idlingResource
    }

}