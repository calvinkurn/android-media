package com.tokopedia.analytics.performance.util

import android.content.Context
import com.tokopedia.device.info.DeviceConnectionInfo
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

    private const val EMBRACE_PRIMARY_CARRIER_KEY = "operatorNameMain"
    private const val EMBRACE_SECONDARY_CARRIER_KEY = "operatorNameSecondary"

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

    fun setCarrierProperties(context: Context) {
        val carriersName: Pair<String, String>? =
            DeviceConnectionInfo.getDualSimCarrierNames(context)

        carriersName?.let {
            Embrace.getInstance().addSessionProperty(
                EMBRACE_PRIMARY_CARRIER_KEY,
                it.first, false
            )
            Embrace.getInstance().addSessionProperty(
                EMBRACE_SECONDARY_CARRIER_KEY,
                it.second, false
            )
        }
    }
}