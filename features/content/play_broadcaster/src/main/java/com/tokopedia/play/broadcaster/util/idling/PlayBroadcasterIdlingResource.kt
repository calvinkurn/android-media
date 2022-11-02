package com.tokopedia.play.broadcaster.util.idling

import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource

/**
 * Created By : Jonathan Darwin on October 11, 2022
 */
object PlayBroadcasterIdlingResource {
    private const val BROADCASTER_IDLING_RESOURCE = "BROADCASTER_IDLING_RESOURCE"
    private val countingIdlingResource = CountingIdlingResource(BROADCASTER_IDLING_RESOURCE)

    val idlingResource: IdlingResource
        get() = countingIdlingResource

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        countingIdlingResource.decrement()
    }
}
