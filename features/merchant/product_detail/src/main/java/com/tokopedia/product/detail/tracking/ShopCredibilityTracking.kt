package com.tokopedia.product.detail.tracking

import com.tokopedia.product.detail.tracking.TrackingConstant.Hit
import com.tokopedia.product.detail.tracking.TrackingConstant.Item
import com.tokopedia.product.detail.tracking.TrackingConstant.Value
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue

object ShopCredibilityTracking {

    private const val ACTION_IMPRESSION_SHOP_TICKER = "impression - shop component ticker"
    private const val ACTION_CLICK_SHOP_TICKER = "click - button text on shop component ticker"
    private const val SHOP_COMPONENT_TICKER = "shop component ticker"

    fun impressShopTicker(
        data: ShopCredibilityTracker.ImpressionShopTicker,
        trackingQueue: TrackingQueue
    ) {

        val shopId = data.shopId
        val componentPosition = data.componentPosition

        val mapEvent = hashMapOf(
            Hit.EVENT to Value.PROMO_VIEW,
            Hit.EVENT_ACTION to ACTION_IMPRESSION_SHOP_TICKER,
            Hit.EVENT_CATEGORY to Value.PRODUCT_DETAIL_PAGE,
            Hit.EVENT_LABEL to "shop_id:$shopId;",
            Hit.BUSINESS_UNIT to Value.PRODUCT_DETAIL_PAGE,
            Hit.COMPONENT to "comp:${data.componentName};temp:${data.componentType};elem:$ACTION_IMPRESSION_SHOP_TICKER;cpos:${componentPosition};",
            Hit.CURRENT_SITE to Value.TOKOPEDIA_MARKETPLACE,
            Hit.LAYOUT to "layout:${data.layoutName};catName:${data.categoryName};catId:${data.categoryId};",
            Hit.PRODUCT_ID to data.productId,
            Hit.ECOMMERCE to mapOf(
                Hit.PROMO_VIEW to mapOf(
                    Hit.PROMOTIONS to listOf(
                        mapOf(
                            Item.CREATIVE_NAME to data.message,
                            Item.CREATIVE_SLOT to componentPosition,
                            Item.ITEM_ID to SHOP_COMPONENT_TICKER,
                            Item.ITEM_NAME to data.tickerType
                        )
                    )
                )
            ),
            Hit.SHOP_ID to "$shopId;",
            Hit.SHOP_TYPE to data.shopType,
            Hit.USER_ID to "${data.userId};"
        )

        trackingQueue.putEETracking(mapEvent)
    }

    fun clickShopTicker(
        data: ShopCredibilityTracker.ClickShopTicker
    ) {

        val shopId = data.shopId

        val mapEvent = hashMapOf<String, Any>(
            Hit.EVENT to "clickPG",
            Hit.EVENT_ACTION to ACTION_CLICK_SHOP_TICKER,
            Hit.EVENT_CATEGORY to Value.PRODUCT_DETAIL_PAGE,
            Hit.EVENT_LABEL to "shop_id:$shopId;title:$SHOP_COMPONENT_TICKER;ticker_type:${data.tickerType};message:${data.message};button_text:${data.buttonText};",
            Hit.BUSINESS_UNIT to Value.PRODUCT_DETAIL_PAGE,
            Hit.COMPONENT to "comp:${data.componentName};temp:${data.componentType};elem:$ACTION_CLICK_SHOP_TICKER;cpos:${data.componentPosition};",
            Hit.CURRENT_SITE to Value.TOKOPEDIA_MARKETPLACE,
            Hit.LAYOUT to "layout:${data.layoutName};catName:${data.categoryName};catId:${data.categoryId};",
            Hit.PRODUCT_ID to data.productId,
            Hit.SHOP_ID to "$shopId;",
            Hit.SHOP_TYPE to data.shopType,
            Hit.USER_ID to "${data.userId};"
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }
}