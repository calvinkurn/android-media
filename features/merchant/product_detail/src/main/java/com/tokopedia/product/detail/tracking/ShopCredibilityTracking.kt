package com.tokopedia.product.detail.tracking

import com.tokopedia.product.detail.common.ProductTrackingConstant
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue

object ShopCredibilityTracking {

    private const val ACTION_IMPRESSION_SHOP_TICKER = "impression - shop component ticker"
    private const val ACTION_CLICK_SHOP_TICKER = "click - button text on shop component ticker"

    fun impressShopTicker(
        data: ShopCredibilityTracker.ImpressionShopTicker,
        trackingQueue: TrackingQueue
    ) {

        val shopId = data.shopId

        val mapEvent = hashMapOf(
            "event" to ProductTrackingConstant.Tracking.PROMO_VIEW,
            "eventAction" to ACTION_IMPRESSION_SHOP_TICKER,
            "eventCategory" to ProductTrackingConstant.Category.PDP,
            "eventLabel" to "shop_id:$shopId;",
            "businessUnit" to ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP,
            "component" to "comp:${data.componentName};temp:${data.componentType};elem:$ACTION_IMPRESSION_SHOP_TICKER;cpos:${data.componentPosition};",
            "currentSite" to ProductTrackingConstant.Tracking.CURRENT_SITE,
            "layout" to "layout:${data.layoutName};catName:${data.categoryName};catId:${data.categoryId};",
            "productId" to data.productId,
            "ecommerce" to mapOf(
                "promoView" to mapOf(
                    "promotions" to listOf(
                        mapOf(
                            "creative_name" to data.message,
                            "creative_slot" to "null",
                            "item_id" to data.title,
                            "item_name" to data.tickerType
                        )
                    )
                )
            ),
            "shopId" to "$shopId;",
            "shopType" to data.shopType,
            "userId" to "${data.userId};"
        )

        trackingQueue.putEETracking(mapEvent)
    }

    fun clickShopTicker(data: ShopCredibilityTracker.ClickShopTicker) {

        val shopId = data.shopId

        val mapEvent = hashMapOf<String, Any>(
            "event" to "clickPG",
            "eventAction" to ACTION_CLICK_SHOP_TICKER,
            "eventCategory" to ProductTrackingConstant.Category.PDP,
            "eventLabel" to "shop_id:$shopId;title:${data.title};ticker_type:${data.tickerType};message:${data.message};button_text:${data.buttonText};",
            "businessUnit" to ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP,
            "component" to "comp:${data.componentName};temp:${data.componentType};elem:$ACTION_CLICK_SHOP_TICKER;cpos:${data.componentPosition};",
            "currentSite" to ProductTrackingConstant.Tracking.CURRENT_SITE,
            "layout" to "layout:${data.layoutName};catName:${data.categoryName};catId:${data.categoryId};",
            "productId" to data.productId,
            "shopId" to "$shopId;",
            "shopType" to data.shopType,
            "userId" to "${data.userId};"
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }
}