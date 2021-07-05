package com.tokopedia.analytics.performance

import androidx.test.espresso.idling.CountingIdlingResource

object PerformanceAnalyticsUtil {
    const val PLT_IDLING_RESOURCE = "plt_idling_resource"
    @JvmField val performanceIdlingResource = CountingIdlingResource(PLT_IDLING_RESOURCE, true)

    fun increment() {
        performanceIdlingResource.increment()
    }

    fun decrement() {
        if (!performanceIdlingResource.isIdleNow) {
            performanceIdlingResource.decrement()
        }
    }
}