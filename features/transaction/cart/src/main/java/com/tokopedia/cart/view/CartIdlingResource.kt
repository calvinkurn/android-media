package com.tokopedia.cart.view

import androidx.annotation.VisibleForTesting
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource

object CartIdlingResource {
    private const val CART_IDLING_RESOURCE = "cart_idling_resource"

    private var idlingResource: CountingIdlingResource? = null

    fun increment() {
        idlingResource?.increment()
    }

    fun decrement() {
        idlingResource?.decrement()
    }

    @VisibleForTesting
    fun getIdlingResource(): IdlingResource {
        idlingResource = CountingIdlingResource(CART_IDLING_RESOURCE)
        return idlingResource!!
    }
}
