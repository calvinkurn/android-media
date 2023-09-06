package com.tokopedia.home_account.privacy_account.tracker

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * Created by Yoris on 21/09/21.
 */
@Deprecated("Remove this class after integrating SCP Login to Tokopedia")
class LinkAccountTracker {

    // tracker no.7
    fun trackViewGojekOTP() {
        track(
            TrackAppUtils.gtmData(
                EVENT_VIEW_ACC_IRIS,
                CATEGORY_GOTO_ACC_LINKING,
                ACTION_VIEW_GOJEK_OTP,
                "")
        )
    }

    // tracker no. 8
    fun trackClickBtnBack() {
        track(
            TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_GOTO_ACC_LINKING,
                ACTION_CLICK_BACK_BTN,
                LABEL_GOJEK_OTP_PAGE)
        )
    }

    // tracker no. 9
    fun trackViewGopayPin() {
        track(
            TrackAppUtils.gtmData(
                EVENT_VIEW_ACC_IRIS,
                CATEGORY_GOTO_ACC_LINKING,
                ACTION_VIEW_GOPAY_PIN,
                "")
        )
    }

    // tracker no. 12
    fun trackClickLewatinToolbar() {
        track(
            TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_GOTO_ACC_LINKING,
                ACTION_CLICK_ON_LEWATIN_DULU_TOOLBAR,
                "")
        )
    }

    // tracker no. 19
    fun trackSkipPopupNo() {
        track(
            TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_GOTO_ACC_LINKING,
                ACTION_CLICK_ON_SAMBUNGIN_GOPAY_LAIN_KALI,
                LABEL_NO),
            clientId = false
        )
    }

    // tracker no. 20
    fun trackSkipPopupYes() {
        track(
            TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_GOTO_ACC_LINKING,
                ACTION_CLICK_ON_SAMBUNGIN_GOPAY_LAIN_KALI,
                LABEL_YES),
            clientId = false
        )
    }

    private fun track(map: MutableMap<String, Any>, clientId: Boolean = true) {
        map[KEY_BUSINESS_UNIT] = BUSSINESS_UNIT
        map[KEY_CURRENT_SITE] = CURRENT_SITE
        if(clientId){
            map[KEY_EXT_CLIENT_ID] = TrackApp.getInstance().gtm.cachedClientIDString
        }
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    companion object {
        const val EVENT_VIEW_ACC_IRIS = "viewAccountIris"
        const val EVENT_CLICK_ACCOUNT = "clickAccount"

        const val ACTION_VIEW_GOJEK_OTP = "view otp gojek page"
        const val ACTION_CLICK_BACK_BTN = "click on button back"
        const val ACTION_VIEW_GOPAY_PIN = "view gopay PIN challenger"
        const val ACTION_CLICK_ON_SAMBUNGIN_GOPAY_LAIN_KALI = "click on sambungin gopay lain kali"
        const val ACTION_CLICK_ON_LEWATIN_DULU_TOOLBAR = "click on lewatin dulu sambungin gopay"

        const val CATEGORY_GOTO_ACC_LINKING = "Goto account linking"

        const val KEY_BUSINESS_UNIT = "businessUnit"
        const val KEY_CURRENT_SITE = "currentSite"
        const val KEY_EXT_CLIENT_ID = "extClientId"

        const val BUSSINESS_UNIT = "user platform"
        const val CURRENT_SITE = "tokopediamarketplace"

        const val LABEL_GOJEK_OTP_PAGE = "gojek otp page"
        const val LABEL_YES = "yes"
        const val LABEL_NO = "no"
    }
}
