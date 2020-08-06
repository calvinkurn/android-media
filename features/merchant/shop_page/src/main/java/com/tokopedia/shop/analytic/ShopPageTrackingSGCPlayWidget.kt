package com.tokopedia.shop.analytic

import com.tokopedia.shop.analytic.ShopPageTrackingConstant.*
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.trackingoptimizer.TrackingQueue

class ShopPageTrackingSGCPlayWidget(
    trackingQueue: TrackingQueue
) : ShopPageTracking(trackingQueue){
    fun onImpressionSGCContent(shopId: String,
                               customDimensionShopPage: CustomDimensionShopPage){
            val map = createMap(CLICK_SHOP_PAGE,
                SHOP_PAGE_SELLER,
                VIEW_WIDGET_BROADCAST,
                shopId,
                customDimensionShopPage)
        map[PAGE_TYPE] = SHOPPAGE
        sendDataLayerEvent(map)
    }

    fun onClickSGCContent(shopId: String,
                           customDimensionShopPage: CustomDimensionShopPage){
            val map = createMap(CLICK_SHOP_PAGE,
                SHOP_PAGE_SELLER,
                    CLICK_WIDGET_BROADCAST,
                shopId,
                customDimensionShopPage)
        map[PAGE_TYPE] = SHOPPAGE
        sendDataLayerEvent(map)
    }
}