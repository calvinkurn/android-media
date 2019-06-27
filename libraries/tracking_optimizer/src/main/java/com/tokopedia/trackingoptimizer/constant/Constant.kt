package com.tokopedia.trackingoptimizer.constant

/**
 * Created by hendry on 27/12/18.
 */
class Constant{
    companion object {
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