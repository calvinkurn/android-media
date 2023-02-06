package com.tokopedia.buyerorderdetail.analytic.tracker

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

object BuyerPartialOrderFulfillmentTracker {

    private val tracker by lazy {
        TrackApp.getInstance().gtm
    }

    fun eventClickTotalAvailableItemPof() {
        val event = mapOf(
            TrackAppUtils.EVENT to
                BuyerOrderDetailTrackerConstant.EVENT_NAME_CLICK_PG,
            TrackAppUtils.EVENT_CATEGORY to
                BuyerOrderDetailTrackerConstant.EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP,
            TrackAppUtils.EVENT_ACTION to
                BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_TOTAL_AVAILABLE_ITEM_POF,
            TrackAppUtils.EVENT_LABEL to String.EMPTY,
            BuyerOrderDetailTrackerConstant.EVENT_KEY_TRACKER_ID to BuyerOrderDetailTrackerConstant.TRACKER_ID_41140,
            BuyerOrderDetailTrackerConstant.EVENT_KEY_BUSINESS_UNIT to
                BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_PHYSICAL_GOODS,
            BuyerOrderDetailTrackerConstant.EVENT_KEY_CURRENT_SITE to
                BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE
        )
        tracker.sendGeneralEvent(event)
    }

    fun eventClickEstimateIconInPopupPof() {
        val event = mapOf(
            TrackAppUtils.EVENT to
                BuyerOrderDetailTrackerConstant.EVENT_NAME_CLICK_PG,
            TrackAppUtils.EVENT_CATEGORY to
                BuyerOrderDetailTrackerConstant.EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP,
            TrackAppUtils.EVENT_ACTION to
                BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_ESTIMATE_ICON_IN_POPUP_POF,
            TrackAppUtils.EVENT_LABEL to String.EMPTY,
            BuyerOrderDetailTrackerConstant.EVENT_KEY_TRACKER_ID to BuyerOrderDetailTrackerConstant.TRACKER_ID_41141,
            BuyerOrderDetailTrackerConstant.EVENT_KEY_BUSINESS_UNIT to
                BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_PHYSICAL_GOODS,
            BuyerOrderDetailTrackerConstant.EVENT_KEY_CURRENT_SITE to
                BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE
        )
        tracker.sendGeneralEvent(event)
    }

    fun eventClickTermsAndConditionsInPopupPof() {
        val event = mapOf(
            TrackAppUtils.EVENT to
                BuyerOrderDetailTrackerConstant.EVENT_NAME_CLICK_PG,
            TrackAppUtils.EVENT_CATEGORY to
                BuyerOrderDetailTrackerConstant.EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP,
            TrackAppUtils.EVENT_ACTION to
                BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_TERMS_AND_CONDITIONS_IN_POPUP_POF,
            TrackAppUtils.EVENT_LABEL to String.EMPTY,
            BuyerOrderDetailTrackerConstant.EVENT_KEY_TRACKER_ID to BuyerOrderDetailTrackerConstant.TRACKER_ID_41142,
            BuyerOrderDetailTrackerConstant.EVENT_KEY_BUSINESS_UNIT to
                BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_PHYSICAL_GOODS,
            BuyerOrderDetailTrackerConstant.EVENT_KEY_CURRENT_SITE to
                BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE
        )
        tracker.sendGeneralEvent(event)
    }

    fun eventClickRejectOrderInPopupPof() {
        val event = mapOf(
            TrackAppUtils.EVENT to
                BuyerOrderDetailTrackerConstant.EVENT_NAME_CLICK_PG,
            TrackAppUtils.EVENT_CATEGORY to
                BuyerOrderDetailTrackerConstant.EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP,
            TrackAppUtils.EVENT_ACTION to
                BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_REJECT_ORDER_IN_POPUP_POF,
            TrackAppUtils.EVENT_LABEL to String.EMPTY,
            BuyerOrderDetailTrackerConstant.EVENT_KEY_TRACKER_ID to BuyerOrderDetailTrackerConstant.TRACKER_ID_41143,
            BuyerOrderDetailTrackerConstant.EVENT_KEY_BUSINESS_UNIT to
                BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_PHYSICAL_GOODS,
            BuyerOrderDetailTrackerConstant.EVENT_KEY_CURRENT_SITE to
                BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE
        )
        tracker.sendGeneralEvent(event)
    }

    fun eventClickConfirmationInPopupPof() {
        val event = mapOf(
            TrackAppUtils.EVENT to
                BuyerOrderDetailTrackerConstant.EVENT_NAME_CLICK_PG,
            TrackAppUtils.EVENT_CATEGORY to
                BuyerOrderDetailTrackerConstant.EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP,
            TrackAppUtils.EVENT_ACTION to
                BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_CONFIRMATION_IN_POPUP_POF,
            TrackAppUtils.EVENT_LABEL to String.EMPTY,
            BuyerOrderDetailTrackerConstant.EVENT_KEY_TRACKER_ID to BuyerOrderDetailTrackerConstant.TRACKER_ID_41144,
            BuyerOrderDetailTrackerConstant.EVENT_KEY_BUSINESS_UNIT to
                BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_PHYSICAL_GOODS,
            BuyerOrderDetailTrackerConstant.EVENT_KEY_CURRENT_SITE to
                BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE
        )
        tracker.sendGeneralEvent(event)
    }

    fun eventClickBackInPopupPofCancel() {
        val event = mapOf(
            TrackAppUtils.EVENT to
                BuyerOrderDetailTrackerConstant.EVENT_NAME_CLICK_PG,
            TrackAppUtils.EVENT_CATEGORY to
                BuyerOrderDetailTrackerConstant.EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP,
            TrackAppUtils.EVENT_ACTION to
                BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_BACK_IN_POPUP_POF_CANCEL,
            TrackAppUtils.EVENT_LABEL to String.EMPTY,
            BuyerOrderDetailTrackerConstant.EVENT_KEY_TRACKER_ID to BuyerOrderDetailTrackerConstant.TRACKER_ID_41151,
            BuyerOrderDetailTrackerConstant.EVENT_KEY_BUSINESS_UNIT to
                BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_PHYSICAL_GOODS,
            BuyerOrderDetailTrackerConstant.EVENT_KEY_CURRENT_SITE to
                BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE
        )
        tracker.sendGeneralEvent(event)
    }

    fun eventClickCancellationInPopupPofCancel() {
        val event = mapOf(
            TrackAppUtils.EVENT to
                BuyerOrderDetailTrackerConstant.EVENT_NAME_CLICK_PG,
            TrackAppUtils.EVENT_CATEGORY to
                BuyerOrderDetailTrackerConstant.EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP,
            TrackAppUtils.EVENT_ACTION to
                BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_CANCELLATION_IN_POPUP_POF_CANCEL,
            TrackAppUtils.EVENT_LABEL to String.EMPTY,
            BuyerOrderDetailTrackerConstant.EVENT_KEY_TRACKER_ID to BuyerOrderDetailTrackerConstant.TRACKER_ID_41152,
            BuyerOrderDetailTrackerConstant.EVENT_KEY_BUSINESS_UNIT to
                BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_PHYSICAL_GOODS,
            BuyerOrderDetailTrackerConstant.EVENT_KEY_CURRENT_SITE to
                BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE
        )
        tracker.sendGeneralEvent(event)
    }
}
