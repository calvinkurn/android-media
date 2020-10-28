package com.tokopedia.trackingoptimizer.constant

/**
 * Created by hendry on 27/12/18.
 */
class Constant{
    companion object {
        const val TRACKING_QUEUE_SIZE_LIMIT_VALUE_REMOTECONFIGKEY = "android_mainapp_analytics_reduce_optimizer_size_limit"
        const val TRACKING_QUEUE_SEND_TRACK_NEW_REMOTECONFIGKEY = "android_enable_tracking_queue_v2_3_98"
        const val EVENT = "event"
        const val EVENT_CATEGORY = "eventCategory"
        const val EVENT_ACTION = "eventAction"
        const val EVENT_LABEL = "eventLabel"
        const val ECOMMERCE = "ecommerce"

        val impressionEventList by lazy{
            listOf("productView", "promoView", "viewProduct")
        }
    }
}