package com.tokopedia.analytics.performance.util

import io.embrace.android.embracesdk.Embrace

object EmbraceMonitoring {
    var ALLOW_EMBRACE_MOMENTS: MutableSet<String> = mutableSetOf(
            EmbraceKey.KEY_MP_HOME,
            EmbraceKey.KEY_PDP_RESULT_TRACE,
            EmbraceKey.KEY_MP_SHOP_HOME_V2,
            EmbraceKey.KEY_SEARCH_RESULT_TRACE,
            EmbraceKey.KEY_ACT_ADD_TO_CART,
            EmbraceKey.KEY_MP_CART,
            EmbraceKey.KEY_MP_CART_INCOMPLETE,
            EmbraceKey.KEY_ACT_BUY,
            EmbraceKey.KEY_DISCOVERY_RESULT_TRACE
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