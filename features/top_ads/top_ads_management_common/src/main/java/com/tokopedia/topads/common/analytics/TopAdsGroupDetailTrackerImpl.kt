package com.tokopedia.topads.common.analytics

import com.tokopedia.topads.common.view.TopadsAutoBidSwitchPartialLayout
import javax.inject.Inject

class TopAdsGroupDetailTrackerImpl @Inject constructor() :
    TopadsAutoBidSwitchPartialLayout.TrackerListener{

    override fun autoBidSwitchClicked(on: Boolean) {
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendAutoBidToggleTopAdsGroupDetailEvent(
            EVENT_ACTION_CLICK_TOGGLE_ATUR_OTOMATIS, if (on) ON else OFF, TRACKER_ID_CLICK_TOGGLE_ATUR_OTOMATIS
        )
    }

    override fun bidChangeConfirmationDialogPositiveClick() {
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendAutoBidToggleTopAdsGroupDetailEvent(
            EVENT_ACTION_CLICK_AKTIFKAN_ATUR_OTOMATIS, "", TRACKER_ID_CLICK_AKTIFKAN_ATUR_OTOMATIS
        )
    }

    override fun bidChangeConfirmationDialogNegativeClick() {
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendAutoBidToggleTopAdsGroupDetailEvent(
            EVENT_ACTION_CLICK_BATALKAN_ATUR_OTOMATIS, "", TRACKER_ID_CLICK_BATALKAN_ATUR_OTOMATIS
        )
    }

    override fun bidChangeToManualLanjuktanClicked() {
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendAutoBidToggleTopAdsGroupDetailEvent(
            EVENT_ACTION_CLICK_AKTIFKAN_ATUR_MANUAL, "", TRACKER_ID_CLICK_AKTIFKAN_ATUR_MANUAL
        )
    }

    override fun bidChangeToManualDismissed() {
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendAutoBidToggleTopAdsGroupDetailEvent(
            EVENT_ACTION_CLICK_BATALKAN_ATUR_MANUAL, "", TRACKER_ID_CLICK_BATALKAN_ATUR_MANUAL
        )
    }
}
