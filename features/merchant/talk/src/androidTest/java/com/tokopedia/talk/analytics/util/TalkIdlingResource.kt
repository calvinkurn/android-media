package com.tokopedia.talk.analytics.util

import androidx.test.espresso.idling.CountingIdlingResource

object TalkIdlingResource {
    private const val RESOURCE = "GLOBAL_TALK"

    @JvmField
    val idlingResource: CountingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        idlingResource.increment()
    }

    fun decrement() {
        idlingResource.decrement()
    }
}