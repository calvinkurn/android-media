package com.tokopedia.changephonenumber.tracker

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

object ChangePhoneNumberTracker {

    private val tracker = TrackApp.getInstance().gtm

    const val BUSSINESS_UNIT = "user platform"
    const val CURRENT_SITE = "tokopediamarketplace"
    const val EVENT_CLICK_ACCOUNT = "clickAccount"
    const val CATEGORY_CHANGE_PHONE = "account setting - change phone"
    const val ACTION_CLICK_BACK = "click on button back"

    private fun track(data: MutableMap<String, Any>) {
        data[BUSSINESS_UNIT] = BUSSINESS_UNIT
        data[CURRENT_SITE] = CURRENT_SITE
        tracker.sendGeneralEvent(data)
    }

    // tracker ID: 28697
    fun trackClickOnBtnBackChangePhone() {
        track(
            TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_CHANGE_PHONE,
                ACTION_CLICK_BACK,
                "")
        )
    }

}