package com.tokopedia.shop.analytic

import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SHOP_PAGE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_WIDGET_BROADCAST
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_ACTION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_CATEGORY
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_LABEL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PAGE_TYPE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_PAGE_SELLER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VIEW_WIDGET_BROADCAST
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