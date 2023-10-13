package com.tokopedia.promousage.util.test

import androidx.annotation.VisibleForTesting
import androidx.test.espresso.idling.CountingIdlingResource

object PromoUsageIdlingResource {

    private const val RESOURCE_NAME = "PromoUsage"

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
