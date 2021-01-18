package com.tokopedia.topchat.common.util

import androidx.annotation.VisibleForTesting
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource

class SimpleIdlingResource {
    companion object {
        private const val RESOURCE = "GLOBAL"
    }

    private var idlingResource: CountingIdlingResource? = null

    fun increment() {
        idlingResource?.increment()
    }

    fun decrement() {
        idlingResource?.decrement()
    }

    @VisibleForTesting
    fun getCountingIdlingResource(resourceName: String = RESOURCE): CountingIdlingResource {
        if(idlingResource == null) {
            idlingResource = CountingIdlingResource(resourceName, true)
        }
        return idlingResource!!
    }
}