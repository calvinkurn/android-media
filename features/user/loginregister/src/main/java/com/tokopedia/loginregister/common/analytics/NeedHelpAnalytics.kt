package com.tokopedia.loginregister.common.analytics

import com.tokopedia.sessioncommon.util.LoginSdkUtils.getClientLabelIfAvailable
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

class NeedHelpAnalytics @Inject constructor() {

    var clientName: String = ""

    //1
    fun trackPageClickButuhBantuan() {
        val data = TrackAppUtils.gtmData(
            EVENT_CLICK_ACCOUNT,
            CATEGORY_LOGIN_PAGE,
            ACTION_CLICK_ON_BUTTON_BUTUH_BANTUAN,
            LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        )
        data[KEY_BUSINESS_UNIT] = BUSINESS_UNIT
        data[KEY_CURRENT_SITE] = CURRENT_SITE

        sendData(data)
    }

    //3
    fun trackPageBottomSheetClickClose(){
        val data = TrackAppUtils.gtmData(
            EVENT_CLICK_ACCOUNT,
            CATEGORY_WIDGET_LOGIN_PAGE,
            ACTION_CLICK_ON_BUTTON_CLOSE_BUTUH_BANTUAN,
            LABEL_WIDGET_BUTUH_BANTUAN + getClientLabelIfAvailable(clientName)
        )
        data[KEY_BUSINESS_UNIT] = BUSINESS_UNIT
        data[KEY_CURRENT_SITE] = CURRENT_SITE

        sendData(data)
    }

    //4
    fun trackPageBottomSheetClickForgotPassword(){
        val data = TrackAppUtils.gtmData(
            EVENT_CLICK_ACCOUNT,
            CATEGORY_WIDGET_LOGIN_PAGE,
            ACTION_CLICK_ON_BUTTON_LUPA_KATA_SANDI,
            LABEL_WIDGET_BUTUH_BANTUAN + getClientLabelIfAvailable(clientName)
        )
        data[KEY_BUSINESS_UNIT] = BUSINESS_UNIT
        data[KEY_CURRENT_SITE] = CURRENT_SITE

        sendData(data)
    }

    //5
    fun trackPageBottomSheetClickInactivePhoneNumber(){
        val data = TrackAppUtils.gtmData(
            EVENT_CLICK_ACCOUNT,
            CATEGORY_WIDGET_LOGIN_PAGE,
            ACTION_CLICK_ON_BUTTON_NOMOR_HP_TIDAK_AKTIF,
            LABEL_WIDGET_BUTUH_BANTUAN + getClientLabelIfAvailable(clientName)
        )
        data[KEY_BUSINESS_UNIT] = BUSINESS_UNIT
        data[KEY_CURRENT_SITE] = CURRENT_SITE

        sendData(data)
    }

    //6
    fun trackPageBottomSheetClickTokopediaCare(){
        val data = TrackAppUtils.gtmData(
            EVENT_CLICK_ACCOUNT,
            CATEGORY_WIDGET_LOGIN_PAGE,
            ACTION_CLICK_ON_BUTTON_HUBUNGI_TOKOPEDIA_CARE,
            LABEL_WIDGET_BUTUH_BANTUAN + getClientLabelIfAvailable(clientName)
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

        private const val CATEGORY_LOGIN_PAGE = "login page"
        private const val CATEGORY_WIDGET_LOGIN_PAGE = "widget login page"

        private const val LABEL_WIDGET_BUTUH_BANTUAN = "widget butuh bantuan"
        private const val LABEL_EMPTY = ""
    }
}
