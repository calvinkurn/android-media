package com.tokopedia.pms.analytics

import androidx.annotation.VisibleForTesting
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource

object PmsIdlingResource {
    private const val RESOURCE = "GLOBAL_PAYMENT_LIST"
    private var idlingResource: CountingIdlingResource? = null

    @VisibleForTesting
    fun getIdlingResource(): IdlingResource? {
        if (idlingResource == null) {
            idlingResource = CountingIdlingResource(RESOURCE)
        }
        return idlingResource
    }

    fun increment() {
        idlingResource?.increment()
    }

    fun decrement() {
        if (idlingResource?.isIdleNow == false) {
            idlingResource?.decrement()
        }
    }
}