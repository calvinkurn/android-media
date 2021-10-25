package com.tokopedia.buyerorderdetail.analytic.tracker

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

object BuyerOrderExtensionTracker {

    private val tracker by lazy {
        TrackApp.getInstance().gtm
    }

    fun eventClickConfirmationOrderExtension(orderId: String) {
        val event = mapOf(
            TrackAppUtils.EVENT to
                    BuyerOrderDetailTrackerConstant.EVENT_NAME_CLICK_PURCHASE_LIST,
            TrackAppUtils.EVENT_CATEGORY to
                    BuyerOrderDetailTrackerConstant.EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP,
            TrackAppUtils.EVENT_ACTION to
                    BuyerOrderDetailTrackerConstant.EVENT_ACTION_CONFIRMATION_ORDER_EXTENSION,
            TrackAppUtils.EVENT_LABEL to orderId,
            BuyerOrderDetailTrackerConstant.EVENT_KEY_BUSINESS_UNIT to
                    BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_PHYSICAL_GOODS,
            BuyerOrderDetailTrackerConstant.EVENT_KEY_CURRENT_SITE to
                    BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE
        )
        tracker.sendGeneralEvent(event)
    }

    fun eventAcceptOrderExtension(orderId: String, pageName: String) {
        val event = mapOf(
            TrackAppUtils.EVENT to
                    BuyerOrderDetailTrackerConstant.EVENT_NAME_CLICK_PURCHASE_LIST,
            TrackAppUtils.EVENT_CATEGORY to
                    BuyerOrderDetailTrackerConstant.EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP,
            TrackAppUtils.EVENT_ACTION to
                    BuyerOrderDetailTrackerConstant.EVENT_ACTION_REQUEST_ACTION_ORDER_EXTENSION,
            TrackAppUtils.EVENT_LABEL to
                    "${BuyerOrderDetailTrackerConstant.EVENT_LABEL_ACCEPT_EXTENSION} - $pageName - $orderId",
            BuyerOrderDetailTrackerConstant.EVENT_KEY_BUSINESS_UNIT to
                    BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_PHYSICAL_GOODS,
            BuyerOrderDetailTrackerConstant.EVENT_KEY_CURRENT_SITE to
                    BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE
        )
        tracker.sendGeneralEvent(event)
    }

    fun eventRejectOrderExtension(orderId: String, pageName: String) {
        val event = mapOf(
            TrackAppUtils.EVENT to
                    BuyerOrderDetailTrackerConstant.EVENT_NAME_CLICK_PURCHASE_LIST,
            TrackAppUtils.EVENT_CATEGORY to
                    BuyerOrderDetailTrackerConstant.EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP,
            TrackAppUtils.EVENT_ACTION to
                    BuyerOrderDetailTrackerConstant.EVENT_ACTION_REQUEST_ACTION_ORDER_EXTENSION,
            TrackAppUtils.EVENT_LABEL to
                    "${BuyerOrderDetailTrackerConstant.EVENT_LABEL_REJECT_EXTENSION} - $pageName - $orderId",
            BuyerOrderDetailTrackerConstant.EVENT_KEY_BUSINESS_UNIT to
                    BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_PHYSICAL_GOODS,
            BuyerOrderDetailTrackerConstant.EVENT_KEY_CURRENT_SITE to
                    BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE
        )
        tracker.sendGeneralEvent(event)
    }
}