package com.tokopedia.product.detail.bseducational.tracker

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.product.detail.common.ProductTrackingConstant
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.trackingoptimizer.TrackingQueue

object ProductEducationalTracker {
    const val CLOSE_BUTTON = "close"
    const val OK_BUTTON = "oke"

    fun onImpressView(trackingQueue: TrackingQueue,
                      position: Int,
                      eduTitle: String,
                      eduDesc: String,
                      productId: String,
                      shopId: String,
                      userId: String,
                      eventCategory: String) {
        val mapEvent = DataLayer.mapOf(
                ProductTrackingConstant.Tracking.KEY_EVENT, "promoView",
                ProductTrackingConstant.Tracking.KEY_CATEGORY, "${ProductTrackingConstant.Category.PDP} - $eventCategory",
                ProductTrackingConstant.Tracking.KEY_ACTION, "impression - $eventCategory",
                ProductTrackingConstant.Tracking.KEY_LABEL, "",
                ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT, ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP,
                ProductTrackingConstant.Tracking.KEY_CURRENT_SITE, ProductTrackingConstant.Tracking.CURRENT_SITE,
                ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT, userId.ifEmpty { null },
                ProductTrackingConstant.Tracking.KEY_ECOMMERCE, DataLayer.mapOf(
                "promoView", DataLayer.mapOf(
                "promotions", DataLayer.listOf(
                DataLayer.mapOf(
                        "id", "$eventCategory - $shopId - $productId",
                        "name", "title:$eduTitle;description:$eduDesc",
                        "creative", "",
                        "position", position
                )
        ))))
        mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID] = productId
        mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = shopId

        trackingQueue.putEETracking(mapEvent as HashMap<String, Any>)
    }

    fun onCloseOrButtonClicked(button: String,
                               eduTitle: String,
                               eduDesc: String,
                               shopId: String,
                               productId: String,
                               userId: String,
                               eventCategory: String) {
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PG,
                "${ProductTrackingConstant.Category.PDP} - $eventCategory",
                "click - cta button on $eventCategory",
                "button:$button;title:$eduTitle;description:$eduDesc")
        mapEvent[ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT] = ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP
        mapEvent[ProductTrackingConstant.Tracking.KEY_CURRENT_SITE] = ProductTrackingConstant.Tracking.CURRENT_SITE
        mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID] = productId
        mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = shopId
        mapEvent[ProductTrackingConstant.Tracking.KEY_HIT_USER_ID] = userId.ifEmpty { null }

        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }
}