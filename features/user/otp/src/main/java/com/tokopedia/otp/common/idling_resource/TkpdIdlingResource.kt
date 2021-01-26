package com.tokopedia.otp.common.idling_resource

import androidx.test.espresso.idling.CountingIdlingResource

class TkpdIdlingResource(resName: String) {

    val countingIdlingResource : CountingIdlingResource? = CountingIdlingResource(resName)

    fun increment() {
        countingIdlingResource?.increment()
    }

    fun decrement() {
        countingIdlingResource?.decrement()
    }
}