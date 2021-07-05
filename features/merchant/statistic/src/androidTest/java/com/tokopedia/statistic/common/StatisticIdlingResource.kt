package com.tokopedia.statistic.common

import androidx.test.espresso.idling.CountingIdlingResource

/**
 * Created By @ilhamsuaib on 10/01/21
 */

object StatisticIdlingResource {
    private const val RESOURCE = "STATISTIC_PAGE"

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