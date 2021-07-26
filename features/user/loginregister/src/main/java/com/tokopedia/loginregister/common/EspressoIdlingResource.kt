package com.tokopedia.loginregister.common

import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResource {
    private const val SOURCE_LOGIN_REGISTER_MODULE = "loginRegister"
    private val countingIdlingResource = CountingIdlingResource(SOURCE_LOGIN_REGISTER_MODULE)

    val idlingResource: IdlingResource
        get() = countingIdlingResource

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        countingIdlingResource.decrement()
    }

}