package com.tokopedia.sellerorder

import androidx.test.espresso.idling.CountingIdlingResource

object SomIdlingResource {
    private const val RESOURCE = "GLOBAL_SOM"

    @JvmField
    val idlingResource: CountingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        idlingResource.increment()
    }

    fun decrement() {
        if (!idlingResource.isIdleNow) {
            idlingResource.decrement()
        }
    }
}