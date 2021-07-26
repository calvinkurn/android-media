package com.tokopedia.home_component.util

import androidx.test.espresso.idling.CountingIdlingResource

object HomeNetworkUtil {
    const val HOME_NETWORK_IDLING_RESOURCE = "home_network_idling_resource"
    @JvmField val homeNetworkIdlingResource = CountingIdlingResource(HOME_NETWORK_IDLING_RESOURCE, true)

    fun increment() {
        homeNetworkIdlingResource.increment()
    }

    fun decrement() {
        if (!homeNetworkIdlingResource.isIdleNow) {
            homeNetworkIdlingResource.decrement()
        }
    }
}