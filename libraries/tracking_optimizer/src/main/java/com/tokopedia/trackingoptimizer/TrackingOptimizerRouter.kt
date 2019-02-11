package com.tokopedia.trackingoptimizer

import android.content.Context

/**
 * put this interface in application level
 */
interface TrackingOptimizerRouter {
    fun sendEventTracking(events: MutableMap<String, Any?>)
    fun sendEnhanceECommerceTracking(events: MutableMap<String, Any?>)
    fun sendTrackDefaultAuth()
    fun sendTrackCustomAuth(context: Context, shopID: String, shopType: String, pageType: String, productId: String)
    fun sendScreenName(screenName: String)
}