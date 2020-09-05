package com.tokopedia.oneclickcheckout.common.idling

import androidx.annotation.VisibleForTesting
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource

object OccIdlingResource {

    private const val RESOURCE_NAME = "OCC"

    private var idlingResource: CountingIdlingResource? = null

    fun increment() {
        idlingResource?.increment()
    }

    fun decrement() {
        idlingResource?.decrement()
    }

    @VisibleForTesting
    fun getIdlingResource(): IdlingResource {
        idlingResource = CountingIdlingResource(RESOURCE_NAME, true)
        return idlingResource!!
    }
}