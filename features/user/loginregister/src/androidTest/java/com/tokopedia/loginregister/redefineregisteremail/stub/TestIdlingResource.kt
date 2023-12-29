package com.tokopedia.loginregister.redefineregisteremail.stub

import androidx.test.espresso.idling.CountingIdlingResource
import com.tokopedia.loginregister.common.utils.BasicIdlingResource

object TestIdlingResourceProvider {
    val countingIdlingResource = CountingIdlingResource("redefineRegister")

    fun instance(): BasicIdlingResource = object : BasicIdlingResource {
        override fun increment() {
            countingIdlingResource.increment()
        }

        override fun decrement() {
            countingIdlingResource.decrement()
        }
    }
}
