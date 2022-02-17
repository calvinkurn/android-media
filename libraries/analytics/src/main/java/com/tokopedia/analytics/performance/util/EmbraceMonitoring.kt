package com.tokopedia.analytics.performance.util

import io.embrace.android.embracesdk.Embrace

object EmbraceMonitoring {
    var ALLOW_EMBRACE_MOMENTS: MutableSet<String> = mutableSetOf()

    fun startMoments(
        eventName: String,
        identifier: String? = null,
        properties: Map<String, Any> = mapOf(),
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
        properties: Map<String, Any> = mapOf(),
    ) {
        if (ALLOW_EMBRACE_MOMENTS.contains(eventName))
            Embrace.getInstance().endEvent(
                eventName,
                identifier,
                properties
            )
    }
}