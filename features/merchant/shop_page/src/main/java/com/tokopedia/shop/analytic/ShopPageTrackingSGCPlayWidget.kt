package com.tokopedia.shop.analytic

import com.tokopedia.shop.analytic.ShopPageTrackingConstant.*
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.*

class ShopPageTrackingSGCPlayWidget(
    trackingQueue: TrackingQueue
) : ShopPageTracking(trackingQueue){
    fun onImpressionSGCContent(shopId: String){
        val eventMap = HashMap<String, Any>()
        eventMap[EVENT] = CLICK_SHOP_PAGE
        eventMap[EVENT_CATEGORY] = SHOP_PAGE_SELLER
        eventMap[EVENT_ACTION] = VIEW_WIDGET_BROADCAST
        eventMap[EVENT_LABEL] = shopId
        eventMap[SHOP_ID] = shopId
        eventMap[PAGE_TYPE] = SHOPPAGE
        sendDataLayerEvent(eventMap)
    }

    fun onClickSGCContent(shopId: String){
        val eventMap = HashMap<String, Any>()
        eventMap[EVENT] = CLICK_SHOP_PAGE
        eventMap[EVENT_CATEGORY] = SHOP_PAGE_SELLER
        eventMap[EVENT_ACTION] = CLICK_WIDGET_BROADCAST
        eventMap[EVENT_LABEL] = shopId
        eventMap[SHOP_ID] = shopId
        eventMap[PAGE_TYPE] = SHOPPAGE
        sendDataLayerEvent(eventMap)
    }
}