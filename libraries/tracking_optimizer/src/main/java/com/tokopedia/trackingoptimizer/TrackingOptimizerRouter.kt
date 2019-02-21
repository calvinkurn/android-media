package com.tokopedia.trackingoptimizer

import android.content.Context

/**
 * put this interface in application level
 */
interface TrackingOptimizerRouter {
    fun sendEventTracking(events: MutableMap<String, Any?>)
    fun sendEnhanceECommerceTracking(events: MutableMap<String, Any?>)
}