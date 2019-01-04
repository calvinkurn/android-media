package com.tokopedia.trackingoptimizer

import android.content.Context

/**
 * Created by hendry on 04/01/19.
 */
interface TrackingOptimizerRouter {
    fun sendEventTracking(events: MutableMap<String, Any?>)
    fun sendEnhanceECommerceTracking(events: MutableMap<String, Any?>)
    fun sendTrackDefaultAuth()
    fun sendTrackCustomAuth(context: Context, shopID: String, shopType: String, pageType: String, productId: String)
    fun sendScreenName(screenName: String)
}