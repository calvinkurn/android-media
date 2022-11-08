package com.tokopedia.search.utils

import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource

object SearchIdlingResource {

    private val countingIdlingResource = CountingIdlingResource("SearchIdlingResource")
    val idlingResource: IdlingResource = countingIdlingResource

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow)
            countingIdlingResource.decrement()
    }
}
