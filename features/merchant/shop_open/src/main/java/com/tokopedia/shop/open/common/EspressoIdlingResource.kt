package com.tokopedia.shop.open.common

import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResource {
    private const val RESOURCE = "IR_SHOP_OPEN"
    private val countingIdlingResource = CountingIdlingResource(RESOURCE)

    val idlingResource: IdlingResource
        get() = countingIdlingResource

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        countingIdlingResource.decrement()
    }
}