package com.tokopedia.profilecompletion.profileinfo.tracker

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

class CloseAccountTracker @Inject constructor() {

    //32749
    fun trackClickCloseAccount(label: String) {
        val data = TrackAppUtils.gtmData(
            EVENT_CLICK_ACCOUNT,
            CATEGORY_ACCOUNT_SETTING_CHANGE_PROFILE,
            ACTION_CLICK_TUTUP_AKUN_BUTTON,
            label
        )
        data.putAll(generateCommon(VALUE_TRACKER_ID_32749))

        sendData(data)
    }

    //32750
    fun trackShowBottomSheet(reason: String) {
        val data = TrackAppUtils.gtmData(
            EVENT_VIEW_ACCOUNT_IRIS,
            CATEGORY_TUTUP_AKUN_BOTTOMSHEET_PAGE,
            ACTION_VIEW_CLOSE_ACCOUNT_PAGE,
            reason
        )
        data.putAll(generateCommon(VALUE_TRACKER_ID_32750))

        sendData(data)
    }

    //32751
    fun trackDismissBottomSheet() {
        val data = TrackAppUtils.gtmData(
            EVENT_CLICK_ACCOUNT,
            CATEGORY_TUTUP_AKUN_BOTTOMSHEET_PAGE,
            ACTION_CLICK_CLOSE_BUTTON,
            EMPTY_STRING
        )
        data.putAll(generateCommon(VALUE_TRACKER_ID_32751))

        sendData(data)
    }

    private fun generateCommon(trackerId: String): Map<String, String> {
        return mapOf(
            KEY_BUSINESS_UNIT to VALUE_BUSINESS_UNIT,
            KEY_CURRENT_SITE to VALUE_CURRENT_SITE,
            KEY_TRACKER_ID to trackerId
        )
    }

    private fun sendData(data: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    companion object {
        private const val KEY_BUSINESS_UNIT = "businessUnit"
        private const val KEY_CURRENT_SITE = "currentSite"
        private const val VALUE_BUSINESS_UNIT = "user platform"
        private const val VALUE_CURRENT_SITE = "tokopediamarketplace"

        private const val EVENT_CLICK_ACCOUNT = "clickAccount"
        private const val EVENT_VIEW_ACCOUNT_IRIS = "viewAccountIris"

        private const val ACTION_CLICK_TUTUP_AKUN_BUTTON = "click tutup akun button"
        private const val ACTION_VIEW_CLOSE_ACCOUNT_PAGE = "view close account page"
        private const val ACTION_CLICK_CLOSE_BUTTON = "click close button"

        private const val CATEGORY_ACCOUNT_SETTING_CHANGE_PROFILE =
            "account setting - change profile"
        private const val CATEGORY_TUTUP_AKUN_BOTTOMSHEET_PAGE = "tutup akun bottomsheet page"

        const val LABEL_KLIK = "click"
        const val LABEL_FAILED = "failed"

        private const val KEY_TRACKER_ID = "trackerId"
        private const val VALUE_TRACKER_ID_32749 = "32749"
        private const val VALUE_TRACKER_ID_32750 = "32750"
        private const val VALUE_TRACKER_ID_32751 = "32751"

        const val REASON_1 = "reason 1"
        const val REASON_2 = "reason 2"
        const val REASON_3 = "reason 3"

        private const val EMPTY_STRING = ""
    }
}
