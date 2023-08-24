package com.tokopedia.product.detail.tracking

import com.tokopedia.product.detail.common.ProductTrackingConstant
import com.tokopedia.product.detail.view.viewholder.a_plus_content.APlusImageUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * MyNakama: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4137
 */
object APlusContentTracking {
    fun trackImpressAPlusMedia(
        trackerData: APlusImageUiModel.TrackerData,
        trackingQueue: TrackingQueue
    ) {
        val eventAction = "impression - a plus content"
        val payloads = hashMapOf<String, Any>(
            "event" to ProductTrackingConstant.Tracking.PROMO_VIEW,
            "eventAction" to eventAction,
            "eventCategory" to ProductTrackingConstant.Category.PDP,
            "eventLabel" to "max_image:${trackerData.mediaCount};",
            "trackerId" to "45822",
            "businessUnit" to ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP,
            "component" to "comp:${trackerData.componentTrackData.componentName};temp:${trackerData.componentTrackData.componentType};elem:$eventAction;cpos:${trackerData.componentTrackData.adapterPosition};",
            "currentSite" to ProductTrackingConstant.Tracking.CURRENT_SITE,
            "layout" to "layout:${trackerData.layoutName};catName:${trackerData.categoryName};catId:${trackerData.categoryId};",
            "productId" to trackerData.productID,
            "ecommerce" to hashMapOf(
                "promoView" to hashMapOf(
                    "promotions" to arrayListOf(
                        hashMapOf(
                            "creative_name" to "null",
                            "creative_slot" to "position:${trackerData.mediaPosition};",
                            "item_id" to "null",
                            "item_name" to "image_url:${trackerData.mediaUrl};"
                        )
                    )
                )
            ),
            "shopId" to trackerData.shopID,
            "userId" to trackerData.userID
        )
        trackingQueue.putEETracking(payloads)
    }

    fun trackClickExpandCollapseToggle(trackerData: APlusImageUiModel.TrackerData) {
        val eventAction = "click - extension content in a plus"
        val payloads = hashMapOf<String, Any>(
            "event" to ProductTrackingConstant.PDP.EVENT_CLICK_PG,
            "eventAction" to eventAction,
            "eventCategory" to ProductTrackingConstant.Category.PDP,
            "eventLabel" to "is_expand:${trackerData.expanded.not()};",
            "trackerId" to "45824",
            "businessUnit" to ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP,
            "component" to "comp:${trackerData.componentTrackData.componentName};temp:${trackerData.componentTrackData.componentType};elem:$eventAction;cpos:${trackerData.componentTrackData.adapterPosition};",
            "currentSite" to ProductTrackingConstant.Tracking.CURRENT_SITE,
            "layout" to "layout:${trackerData.layoutName};catName:${trackerData.categoryName};catId:${trackerData.categoryId};",
            "productId" to trackerData.productID,
            "shopId" to trackerData.shopID,
            "userId" to trackerData.userID
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(payloads)
    }
}
