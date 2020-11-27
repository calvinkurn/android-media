package com.tokopedia.home.analytics.v2

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.*

object RechargeBUWidgetTracking : BaseTracking() {

    fun sendEvent(trackingQueue: TrackingQueue, trackingDataString: String, data: Map<String, Any>? = null) {
        val trackingData = Gson().fromJson<HashMap<String, Any>>(trackingDataString, object : TypeToken<HashMap<String, Any>>() {}.type)
        data?.run {
            for ((key, value) in data) {
                trackingData[key] = value
            }
        }
        trackingQueue.putEETracking(trackingData)
    }
}