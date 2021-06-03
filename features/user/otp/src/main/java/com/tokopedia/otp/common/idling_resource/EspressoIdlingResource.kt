package com.tokopedia.otp.common.idling_resource

import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResource {
    private const val SOURCE_VERIFICATION_MODULE = "verification"
    private val countingIdlingResource = CountingIdlingResource(SOURCE_VERIFICATION_MODULE)

    val idlingResource: IdlingResource
        get() = countingIdlingResource

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        countingIdlingResource.decrement()
    }

}