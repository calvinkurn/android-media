package com.tokopedia.deals.ui.pdp.common

import androidx.annotation.VisibleForTesting
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource

object DealsPDPIdlingResource {
    private const val RESOURCE_NAME = "DEALS_PDP"

    private var idlingResource: CountingIdlingResource? = null

    fun increment() {
        com.tokopedia.deals.ui.pdp.common.DealsPDPIdlingResource.idlingResource?.increment()
    }

    fun decrement() {
        com.tokopedia.deals.ui.pdp.common.DealsPDPIdlingResource.idlingResource?.decrement()
    }

    @VisibleForTesting
    fun getIdlingResource(): IdlingResource? {
        com.tokopedia.deals.ui.pdp.common.DealsPDPIdlingResource.idlingResource = CountingIdlingResource(
            com.tokopedia.deals.ui.pdp.common.DealsPDPIdlingResource.RESOURCE_NAME
        )
        return com.tokopedia.deals.ui.pdp.common.DealsPDPIdlingResource.idlingResource
    }

}
