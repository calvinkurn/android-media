package com.tokopedia.product.detail.tracking

import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.tracking.TrackingConstant.Hit
import com.tokopedia.product.detail.tracking.TrackingConstant.Value
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue

object ProductDetailBottomSheetTracking {

    private const val ACTION_IMPRESSION_INFO_ITEM = "impression - product detail bottomsheet"
    private const val CATEGORY_PRODUCT_DETAIL_BOTTOMSHEET = "product detail page - product detail bottomsheet"
    private const val ACTION_CLICK_INFO_PRODUCT_DETAIL_BOTTOMSHEET = "click clickable information on product detail bottomsheet"

    fun impressInfoItem(
        productInfo: DynamicProductInfoP1,
        userId: String,
        infoTitle: String,
        infoValue: String,
        position: Int,
        trackingQueue: TrackingQueue
    ){
        val common = CommonTracker(productInfo, userId)
        val productId = common.productId

        val mapEvent = hashMapOf<String, Any>(
            Hit.EVENT to Value.PROMO_VIEW,
            Hit.EVENT_ACTION to ACTION_IMPRESSION_INFO_ITEM,
            Hit.EVENT_CATEGORY to CATEGORY_PRODUCT_DETAIL_BOTTOMSHEET,
            Hit.EVENT_LABEL to "",
            Hit.BUSINESS_UNIT to Value.PRODUCT_DETAIL_PAGE,
            Hit.COMPONENT to "",
            Hit.CURRENT_SITE to Value.TOKOPEDIA_MARKETPLACE,
            Hit.LAYOUT to "layout:${common.layoutName};catName:${common.categoryName};catId:${common.categoryId};",
            Hit.PRODUCT_ID to productId,
            Hit.ECOMMERCE to hashMapOf(
                Hit.PROMO_VIEW to hashMapOf(
                    Hit.PROMOTIONS to arrayListOf(
                        hashMapOf(
                            TrackingConstant.Item.CREATIVE_NAME to infoValue,
                            TrackingConstant.Item.CREATIVE_SLOT to position,
                            TrackingConstant.Item.ITEM_ID to "product detail bottomsheet - $productId",
                            TrackingConstant.Item.ITEM_NAME to infoTitle
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

    fun clickInfoItem(
        productInfo: DynamicProductInfoP1,
        userId: String,
        infoTitle: String,
        infoValue: String,
        position: Int
    ){

        val common = CommonTracker(productInfo, userId)
        val productId = common.productId

        val mapEvent = hashMapOf<String, Any>(
            Hit.EVENT to Value.SELECT_CONTENT,
            Hit.EVENT_ACTION to ACTION_CLICK_INFO_PRODUCT_DETAIL_BOTTOMSHEET,
            Hit.EVENT_CATEGORY to CATEGORY_PRODUCT_DETAIL_BOTTOMSHEET,
            Hit.EVENT_LABEL to "",
            Hit.BUSINESS_UNIT to Value.PRODUCT_DETAIL_PAGE,
            Hit.COMPONENT to "",
            Hit.CURRENT_SITE to Value.TOKOPEDIA_MARKETPLACE,
            Hit.LAYOUT to "layout:${common.layoutName};catName:${common.categoryName};catId:${common.categoryId};",
            Hit.PRODUCT_ID to common.productId,
            Hit.ECOMMERCE to hashMapOf(
                Hit.PROMO_VIEW to hashMapOf(
                    Hit.PROMOTIONS to arrayListOf(
                        hashMapOf(
                            TrackingConstant.Item.CREATIVE_NAME to infoValue,
                            TrackingConstant.Item.CREATIVE_SLOT to position,
                            TrackingConstant.Item.ITEM_ID to "product detail bottomsheet - $productId",
                            TrackingConstant.Item.ITEM_NAME to infoTitle
                        )
                    )
                )
            ),
            Hit.SHOP_ID to "${common.shopId};",
            Hit.SHOP_TYPE to common.shopType,
            Hit.USER_ID to "${common.userId};"
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }
}