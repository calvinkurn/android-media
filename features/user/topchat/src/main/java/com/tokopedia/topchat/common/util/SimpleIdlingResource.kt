package com.tokopedia.topchat.common.util

import androidx.annotation.VisibleForTesting
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource

object SimpleIdlingResource {
    private const val RESOURCE = "GLOBAL"

    private var idlingResource: CountingIdlingResource? = null

    fun increment() {
        idlingResource?.increment()
    }

    fun decrement() {
        idlingResource?.decrement()
    }

    @VisibleForTesting
    fun getIdlingResource(): IdlingResource {
        idlingResource = CountingIdlingResource(RESOURCE, true)
        return idlingResource!!
    }
}