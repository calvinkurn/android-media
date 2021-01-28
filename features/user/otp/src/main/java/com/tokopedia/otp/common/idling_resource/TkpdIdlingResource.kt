package com.tokopedia.otp.common.idling_resource

import androidx.annotation.VisibleForTesting
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource

object TkpdIdlingResource {
    private var idlingResource: CountingIdlingResource? = null

    fun increment() {
        idlingResource!!.increment()
    }

    fun decrement() {
        idlingResource!!.decrement()
    }

    @VisibleForTesting
    fun getIdlingResource(resName: String): IdlingResource {
        idlingResource = CountingIdlingResource(resName)
        return idlingResource!!
    }
}