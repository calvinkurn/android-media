package com.tokopedia.home.analytics.v2

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.*

object RechargeBUWidgetTracking : BaseTracking() {

    fun enhanceEcommerceTracker(trackingQueue: TrackingQueue, trackingDataString: String) {
        val trackingData = Gson().fromJson<HashMap<String, Any>>(trackingDataString, object : TypeToken<HashMap<String, Any>>() {}.type)
        trackingQueue.putEETracking(trackingData)
    }
}