package com.tokopedia.topads.dashboard.tracker

import com.tokopedia.topads.common.analytics.*
import com.tokopedia.topads.common.view.TopadsAutoBidSwitchPartialLayout
import com.tokopedia.topads.dashboard.view.sheet.BidSwitchManualBudgetBottomSheet
import javax.inject.Inject

class TopAdsGroupDetailTrackerImpl @Inject constructor() : TopadsAutoBidSwitchPartialLayout.TrackerListener, BidSwitchManualBudgetBottomSheet.TrackerListener {

    override fun autoBidSwitchClicked(on: Boolean) {
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendAutoBidToggleTopAdsGroupDetailEvent(
            CLICK_TOGGLE_ATUR_OTOMATIS, if(on) ON else OFF
        )
    }

    override fun bidChangeConfirmationDialogPositiveClick(isAutomatic: Boolean) {
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendAutoBidToggleTopAdsGroupDetailEvent(
            CLICK_AKTIFKAN_ATUR_OTOMATIS, ""
        )
    }

    override fun bidChangeConfirmationDialogNegativeClick(isAutomatic: Boolean) {
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendAutoBidToggleTopAdsGroupDetailEvent(
            CLICK_BATALKAN_ATUR_OTOMATIS, ""
        )
    }

    override fun manualBidBottomSheetLanjuktanClicked() {
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendAutoBidToggleTopAdsGroupDetailEvent(
            CLICK_AKTIFKAN_ATUR_MANUAL, ""
        )
    }

    override fun manualBidBottomSheetDismissed() {
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendAutoBidToggleTopAdsGroupDetailEvent(
            CLICK_BATALKAN_ATUR_MANUAL, ""
        )
    }
}