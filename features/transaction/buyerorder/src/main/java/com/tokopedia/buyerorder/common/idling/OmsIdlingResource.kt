package com.tokopedia.buyerorder.common.idling

import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource

/**
 * created by @bayazidnasir on 6/9/2022
 */

object OmsIdlingResource {

    private const val RESOURCE_NAME = "buyerorder"

    private var idlingResource: CountingIdlingResource? = null

    fun increment() {
        idlingResource?.increment()
    }

    fun decrement() {
        idlingResource?.decrement()
    }

    fun getIdlingResource(): IdlingResource? {
        idlingResource = CountingIdlingResource(RESOURCE_NAME)
        return idlingResource
    }
}