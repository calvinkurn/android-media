package com.tokopedia.product.detail.tracking

import com.tokopedia.product.detail.tracking.TrackingConstant.Hit
import com.tokopedia.product.detail.tracking.TrackingConstant.Item
import com.tokopedia.product.detail.tracking.TrackingConstant.Value
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue

object ProductDetailNavigationTracking {

    private const val ACTION_IMPRESSION_NAVIGATION = "impression - pdp navigation bar"
    private const val ACTION_CLICK_NAVIGATION = "click - pdp navigation bar"

    private const val ITEM_NAME = "pdp navbar"

    fun impressNavigation(
        common: CommonTracker,
        data: ProductDetailNavigationTracker,
        trackingQueue: TrackingQueue
    ) {
        val productId = common.productId

        val mapEvent = hashMapOf<String, Any>(
            Hit.EVENT to Value.PROMO_VIEW,
            Hit.EVENT_ACTION to ACTION_IMPRESSION_NAVIGATION,
            Hit.EVENT_CATEGORY to Value.PRODUCT_DETAIL_PAGE,
            Hit.EVENT_LABEL to "",
            Hit.BUSINESS_UNIT to Value.PRODUCT_DETAIL_PAGE,
            Hit.COMPONENT to "comp:${common.componentName};temp:${common.componentType};elem:$ACTION_IMPRESSION_NAVIGATION;cpos:${common.componentPosition};",
            Hit.CURRENT_SITE to Value.TOKOPEDIA_MARKETPLACE,
            Hit.LAYOUT to "layout:${common.layoutName};catName:${common.categoryName};catId:${common.categoryId};",
            Hit.PRODUCT_ID to productId,
            Hit.ECOMMERCE to hashMapOf(
                Hit.PROMO_VIEW to hashMapOf(
                    Hit.PROMOTIONS to arrayListOf(
                        hashMapOf(
                            Item.CREATIVE_NAME to productId,
                            Item.CREATIVE_SLOT to data.buttonPosition,
                            Item.ITEM_ID to data.buttonName,
                            Item.ITEM_NAME to ITEM_NAME
                        )
                    )
                )
            ),
            Hit.SHOP_ID to "${common.shopId};",
            Hit.SHOP_TYPE to common.shopType,
            Hit.USER_ID to "${common.userId};"
        )
        trackingQueue.putEETracking(mapEvent)
    }

    fun clickNavigation(common: CommonTracker, data: ProductDetailNavigationTracker) {
        val mapEvent = hashMapOf<String, Any>(
            Hit.EVENT to "clickPG",
            Hit.EVENT_ACTION to ACTION_CLICK_NAVIGATION,
            Hit.EVENT_CATEGORY to Value.PRODUCT_DETAIL_PAGE,
            Hit.EVENT_LABEL to "product_id:${common.productId};button_name:${data.buttonName};button_position:${data.buttonPosition};",
            Hit.BUSINESS_UNIT to Value.PRODUCT_DETAIL_PAGE,
            Hit.COMPONENT to "comp:${common.componentName};temp:${common.componentType};elem:$ACTION_CLICK_NAVIGATION;cpos:${common.componentPosition};",
            Hit.CURRENT_SITE to Value.TOKOPEDIA_MARKETPLACE,
            Hit.LAYOUT to "layout:${common.layoutName};catName:${common.categoryName};catId:${common.categoryId};",
            Hit.PRODUCT_ID to common.productId,
            Hit.SHOP_ID to "${common.shopId};",
            Hit.SHOP_TYPE to common.shopType,
            Hit.USER_ID to "${common.userId};"
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }
}