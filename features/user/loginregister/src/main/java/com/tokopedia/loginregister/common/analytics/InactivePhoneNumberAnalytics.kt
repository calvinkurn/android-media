package com.tokopedia.loginregister.common.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class InactivePhoneNumberAnalytics {

    //1
    fun trackPageClickButuhBantuan() {
        val data = TrackAppUtils.gtmData(
            EVENT_CLICK_ACCOUNT,
            ACTION_CLICK_ON_BUTTON_BUTUH_BANTUAN,
            CATEGORY_LOGIN_PAGE,
            LABEL_EMPTY
        )
        data[KEY_BUSINESS_UNIT] = BUSINESS_UNIT
        data[KEY_CURRENT_SITE] = CURRENT_SITE

        sendData(data)
    }

    //3
    fun trackPageBottomSheetClickClose(){
        val data = TrackAppUtils.gtmData(
            EVENT_CLICK_ACCOUNT,
            ACTION_CLICK_ON_BUTTON_CLOSE_BUTUH_BANTUAN,
            CATEGORY_WIDGET_LOGIN_PAGE,
            LABEL_WIDGET_BUTUH_BANTUAN
        )
        data[KEY_BUSINESS_UNIT] = BUSINESS_UNIT
        data[KEY_CURRENT_SITE] = CURRENT_SITE

        sendData(data)
    }

    //4
    fun trackPageBottomSheetClickForgotPassword(){
        val data = TrackAppUtils.gtmData(
            EVENT_CLICK_ACCOUNT,
            ACTION_CLICK_ON_BUTTON_LUPA_KATA_SANDI,
            CATEGORY_WIDGET_LOGIN_PAGE,
            LABEL_WIDGET_BUTUH_BANTUAN
        )
        data[KEY_BUSINESS_UNIT] = BUSINESS_UNIT
        data[KEY_CURRENT_SITE] = CURRENT_SITE

        sendData(data)
    }

    //5
    fun trackPageBottomSheetClickInactivePhoneNumber(){
        val data = TrackAppUtils.gtmData(
            EVENT_CLICK_ACCOUNT,
            ACTION_CLICK_ON_BUTTON_NOMOR_HP_TIDAK_AKTIF,
            CATEGORY_WIDGET_LOGIN_PAGE,
            LABEL_WIDGET_BUTUH_BANTUAN
        )
        data[KEY_BUSINESS_UNIT] = BUSINESS_UNIT
        data[KEY_CURRENT_SITE] = CURRENT_SITE

        sendData(data)
    }

    //6
    fun trackPageBottomSheetClickTokopediaCare(){
        val data = TrackAppUtils.gtmData(
            EVENT_CLICK_ACCOUNT,
            ACTION_CLICK_ON_BUTTON_HUBUNGI_TOKOPEDIA_CARE,
            CATEGORY_WIDGET_LOGIN_PAGE,
            LABEL_WIDGET_BUTUH_BANTUAN
        )
        data[KEY_BUSINESS_UNIT] = BUSINESS_UNIT
        data[KEY_CURRENT_SITE] = CURRENT_SITE

        sendData(data)
    }

    //7
    fun trackPageInactivePhoneNumberClickNext(labelAction: String, errorMessage: String = "", inactivePhoneNumber: String = ""){

        val label = if (labelAction == LABEL_CLICK || labelAction == LABEL_SUCCESS)
            labelAction
        else
            labelErrorMessage(labelAction, errorMessage, inactivePhoneNumber)

        val data = TrackAppUtils.gtmData(
            EVENT_CLICK_ACCOUNT,
            ACTION_CLICK_ON_BUTTON_LANJUT,
            CATEGORY_ACCOUNT_SETTING,
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
            ACTION_CLICK_ON_BUTTON_BACK,
            CATEGORY_ACCOUNT_SETTING,
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

        private const val ACTION_CLICK_ON_BUTTON_BUTUH_BANTUAN = "click on button butuh bantuan"
        private const val ACTION_CLICK_ON_BUTTON_CLOSE_BUTUH_BANTUAN = "click on button close butuh bantuan"
        private const val ACTION_CLICK_ON_BUTTON_LUPA_KATA_SANDI = "click on button lupa kata sandi"
        private const val ACTION_CLICK_ON_BUTTON_NOMOR_HP_TIDAK_AKTIF = "click on button nomor hp tidak aktif"
        private const val ACTION_CLICK_ON_BUTTON_HUBUNGI_TOKOPEDIA_CARE = "click on button hubungi tokopedia care"
        private const val ACTION_CLICK_ON_BUTTON_LANJUT = "click on button lanjut"
        private const val ACTION_CLICK_ON_BUTTON_BACK = "click on button back"

        private const val CATEGORY_LOGIN_PAGE = "login page"
        private const val CATEGORY_WIDGET_LOGIN_PAGE = "widget login page"
        private const val CATEGORY_ACCOUNT_SETTING = "account setting - change phone number"

        private const val LABEL_WIDGET_BUTUH_BANTUAN = "widget butuh bantuan"
        private const val LABEL_INACTIVE_PHONE_NUMBER = "inactive phone number"
        private const val LABEL_EMPTY = ""

        const val LABEL_CLICK = "click"
        const val LABEL_SUCCESS = "success"
        const val LABEL_FAILED = "failed"

        private fun labelErrorMessage(label: String, errorMessage: String, inactivePhoneNumber: String): String{
            return "$label - $errorMessage - $inactivePhoneNumber"
        }

    }
}