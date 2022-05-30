package com.tokopedia.updateinactivephone.common.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class InputOldPhoneNumberAnalytics {

    //7
    fun trackPageInactivePhoneNumberClickNext(labelAction: String, errorMessage: String = "", inactivePhoneNumber: String = ""){

        val label = if (labelAction == LABEL_CLICK || labelAction == LABEL_SUCCESS)
            labelAction
        else
            labelErrorMessage(labelAction, errorMessage, inactivePhoneNumber)

        val data = TrackAppUtils.gtmData(
            EVENT_CLICK_ACCOUNT,
            CATEGORY_ACCOUNT_SETTING,
            ACTION_CLICK_ON_BUTTON_LANJUT,
            label
        )
        data[KEY_BUSINESS_UNIT] = BUSINESS_UNIT
        data[KEY_CURRENT_SITE] = CURRENT_SITE

        sendData(data)
    }

    //8
    fun trackPageInactivePhoneNumberClickBack(){
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

    private fun sendData(data: Map<String, Any>){
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    companion object {
        private const val KEY_BUSINESS_UNIT = "businessUnit"
        private const val KEY_CURRENT_SITE = "currentSite"

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

        private fun labelErrorMessage(label: String, errorMessage: String, inactivePhoneNumber: String): String{
            return "$label - $errorMessage - $inactivePhoneNumber"
        }

    }
}