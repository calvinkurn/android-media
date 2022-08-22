package com.tokopedia.updateinactivephone.common.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class InputOldPhoneNumberAnalytics {

    //Tracker ID: 30678
    fun trackPageInactivePhoneNumberClickNext(labelAction: String, errorMessage: String = "") {

        val label = if (labelAction == LABEL_CLICK || labelAction == LABEL_SUCCESS)
            "$labelAction - $LABEL_INACTIVE_PHONE_NUMBER"
        else
            labelErrorMessage(labelAction, errorMessage)

        val data = TrackAppUtils.gtmData(
            EVENT_CLICK_ACCOUNT,
            CATEGORY_ACCOUNT_SETTING,
            ACTION_CLICK_ON_BUTTON_LANJUT,
            label
        )
        data[KEY_TRACKER_ID] = VALUE_TRACKER_30678
        data[KEY_BUSINESS_UNIT] = BUSINESS_UNIT
        data[KEY_CURRENT_SITE] = CURRENT_SITE

        sendData(data)
    }

    //Tracker ID: 30679
    fun trackPageInactivePhoneNumberClickBack() {
        val data = TrackAppUtils.gtmData(
            EVENT_CLICK_ACCOUNT,
            CATEGORY_ACCOUNT_SETTING,
            ACTION_CLICK_ON_BUTTON_BACK,
            LABEL_INACTIVE_PHONE_NUMBER
        )
        data[KEY_BUSINESS_UNIT] = BUSINESS_UNIT
        data[KEY_CURRENT_SITE] = CURRENT_SITE

        sendData(data)
    }

    private fun sendData(data: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    private fun labelErrorMessage(label: String, errorMessage: String): String {
        return "$label - $errorMessage - $LABEL_INACTIVE_PHONE_NUMBER"
    }

    companion object {
        private const val KEY_BUSINESS_UNIT = "businessUnit"
        private const val KEY_CURRENT_SITE = "currentSite"
        private const val KEY_TRACKER_ID = "trackerId"
        private const val VALUE_TRACKER_30678 = "30678"

        private const val BUSINESS_UNIT = "user platform"
        private const val CURRENT_SITE = "tokopediamarketplace"

        private const val EVENT_CLICK_ACCOUNT = "clickAccount"

        private const val ACTION_CLICK_ON_BUTTON_LANJUT = "click on button lanjut"
        private const val ACTION_CLICK_ON_BUTTON_BACK = "click on button back"

        private const val CATEGORY_ACCOUNT_SETTING = "account setting - change phone number"

        private const val LABEL_INACTIVE_PHONE_NUMBER = "inactive phone number"

        const val LABEL_CLICK = "click"
        const val LABEL_SUCCESS = "success"
        const val LABEL_FAILED = "failed"
    }
}