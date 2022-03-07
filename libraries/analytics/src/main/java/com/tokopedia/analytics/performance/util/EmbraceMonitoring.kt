package com.tokopedia.analytics.performance.util

import io.embrace.android.embracesdk.Embrace

object EmbraceMonitoring {
    var ALLOW_EMBRACE_MOMENTS: MutableSet<String> = mutableSetOf(
        "mp_home",
        "pdp_result_trace",
        "mp_shop_home_v2",
        "search_result_trace",
        "act_add_to_cart",
        "mp_cart",
        "act_buy",
        "discovery_result_trace"
    )

    fun startMoments(
        eventName: String,
        identifier: String? = null,
        properties: Map<String, Any> = emptyMap(),
        allowScreenshot: Boolean = false
    ) {
        if (ALLOW_EMBRACE_MOMENTS.contains(eventName))
            Embrace.getInstance().startEvent(
                eventName,
                identifier,
                allowScreenshot,
                properties
            )
    }

    fun stopMoments(
        eventName: String,
        identifier: String? = null,
        properties: Map<String, Any> = emptyMap(),
    ) {
        if (ALLOW_EMBRACE_MOMENTS.contains(eventName))
            Embrace.getInstance().endEvent(
                eventName,
                identifier,
                properties
            )
    }

    fun logBreadcrumb(message: String) {
        Embrace.getInstance().logBreadcrumb(message)
    }
}