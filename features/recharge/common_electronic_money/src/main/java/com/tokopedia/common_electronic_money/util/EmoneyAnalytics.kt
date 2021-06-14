package com.tokopedia.common_electronic_money.util

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import kotlin.collections.HashMap

class EmoneyAnalytics {

    private fun addComponentClickNFC(mapEvent: MutableMap<String, Any>, userId: String, irisSessionId: String) {
        mapEvent[Param.LOGGED_IN_STATUS] = if (userId.isNotEmpty()) "true" else "false"
        mapEvent[Param.CURRENT_SITE] = Param.CURRENT_SITE_EMONEY
        mapEvent[Param.BUSINESS_UNIT] = Param.BUSINESS_UNIT_EMONEY
        mapEvent[Param.SESSION_IRIS] = irisSessionId
        mapEvent[Param.USER_ID] = userId
        mapEvent[Param.CLIENT_ID] = "none"

        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    private fun addComponentOpenScreenNFC(screenName: String, userId: String, irisSessionId: String) {
        val mapEvent = HashMap<String, String>()
        mapEvent[Param.EVENT] = Event.OPEN_SCREEN
        mapEvent[Param.CURRENT_SITE] = Param.CURRENT_SITE_EMONEY
        mapEvent[Param.BUSINESS_UNIT] = Param.BUSINESS_UNIT_EMONEY
        mapEvent[Param.SESSION_IRIS] = irisSessionId
        mapEvent[Param.USER_ID] = userId

        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName, mapEvent)
    }

    fun openScreenNFC(operatorName: String, userId: String, irisSessionId: String) {
        val stringScreenName = "${Screen.INITIAL_NFC}-${operatorName}"
        addComponentOpenScreenNFC(stringScreenName, userId, irisSessionId)
    }

    fun openScreenReadingCardNFC(operatorName: String, userId: String, irisSessionId: String) {
        val stringScreenName = "${Screen.READING_NFC}-${operatorName}"
        addComponentOpenScreenNFC(stringScreenName, userId, irisSessionId)
    }

    fun openScreenSuccessReadCardNFC(operatorName: String, userId: String, irisSessionId: String) {
        val stringScreenName = "${Screen.SUCCESS_NFC}-${operatorName}"
        addComponentOpenScreenNFC(stringScreenName, userId, irisSessionId)
    }

    fun openScreenFailedReadCardNFC(userId: String, irisSessionId: String) {
        val stringScreenName = "${Screen.FAILED_NFC}"
        addComponentOpenScreenNFC(stringScreenName, userId, irisSessionId)
    }

    fun clickBtnCloseCheckSaldoNFC(action: String, screenName: String, category: String, operator: String,
                                   userId: String, irisSessionId: String) {
        val map = TrackAppUtils.gtmData(
                Event.CLICK_NFC,
                Category.DIGITAL_NFC,
                action,
                "")
        addComponentClickNFC(map,userId, irisSessionId)
    }

    fun clickTopupEmoney(category: String, operator:String, userId: String, irisSessionId: String) {
        val map = TrackAppUtils.gtmData(
                Event.CLICK_NFC,
                Category.DIGITAL_NFC,
                Action.CLICK_TOPUP,
                "")
        addComponentClickNFC(map, userId, irisSessionId)
    }

    fun clickTryAgainTapEmoney(category: String, userId: String, irisSessionId: String) {
        val map = TrackAppUtils.gtmData(
                Event.CLICK_NFC,
                Category.DIGITAL_NFC,
                Action.CLICK_TRY_AGAIN,
                "")
        addComponentClickNFC(map, userId, irisSessionId)
    }

    fun onTapEmoneyCardShowLoading(userId: String, irisSessionId: String) {
        val map = TrackAppUtils.gtmData(
                Event.CLICK_NFC,
                Category.DIGITAL_NFC,
                Action.CHECK_STEP_2,
                "")
        addComponentClickNFC(map, userId, irisSessionId)
    }

    fun onShowLastBalance(cardNumber:String?, balance:Int?, userId: String, irisSessionId: String) {
        val map = TrackAppUtils.gtmData(
                Event.CLICK_NFC,
                Category.DIGITAL_NFC,
                Action.SUCCESS_CHECK_BALANCE,
                "$cardNumber - $balance")
        addComponentClickNFC(map, userId, irisSessionId)
    }

    fun onShowErrorTracking(userId: String, irisSessionId: String) {
        val map = TrackAppUtils.gtmData(
                Event.CLICK_NFC,
                Category.DIGITAL_NFC,
                Action.FAILED_UPDATE_BALANCE,
                "")
        addComponentClickNFC(map, userId, irisSessionId)
    }

    //---------------------------------------------------------------------------------------------------------
    // OLD ANALYTICS

    fun onActivateNFCFromSetting() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_NFC,
                Category.DIGITAL_NFC,
                Action.CLICK_ACTIVATE_PROMPT,
                Label.EMONEY
        ))
    }

    fun onCancelActivateNFCFromSetting() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_NFC,
                Category.DIGITAL_NFC,
                Action.CLICK_CANCEL_PROMPT,
                Label.EMONEY))
    }

    fun onEnableNFC() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_NFC,
                Category.DIGITAL_NFC,
                Action.CHECK_STEP_1,
                Label.EMONEY
        ))
    }

    fun onErrorReadingCard() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_NFC,
                Category.DIGITAL_NFC,
                Action.CARD_IS_NOT_SUPPORTED,
                Label.EMONEY
        ))
    }

    fun onClickActivateNFC() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_NFC,
                Category.DIGITAL_NFC,
                Action.CLICK_ACTIVATE,
                Label.EMONEY
        ))
    }

    interface Event {
        companion object {
            const val CLICK_NFC = "clickNFC"
            const val OPEN_SCREEN = "openScreen"
        }
    }

    interface Screen {
        companion object {
            const val INITIAL_NFC = "/initial-nfc-page"
            const val READING_NFC = "/reading-card-nfc"
            const val SUCCESS_NFC = "/success-cek-saldo-nfc"
            const val FAILED_NFC = "/failed-cek-saldo-nfc"
        }
    }

    interface Category {
        companion object {
            const val DIGITAL_NFC = "digital - nfc page"
        }
    }

    interface Action {
        companion object {
            const val CLICK_ACTIVATE = "click aktifkan"
            const val CLICK_ACTIVATE_PROMPT = "click aktifkan_prompt"
            const val CLICK_CANCEL_PROMPT = "click batalkan prompt"
            const val CHECK_STEP_1 = "check step - 1"
            const val CHECK_STEP_2 = "check step - 2"
            const val CARD_IS_NOT_SUPPORTED = "card not supported"
            const val FAILED_UPDATE_BALANCE = "failed update saldo"
            const val SUCCESS_CHECK_BALANCE = "success check saldo"

            const val CLICK_TOPUP = "click top up now"
            const val CLICK_TRY_AGAIN = "click coba lagi"
            const val CLICK_CLOSE_INITAL_PAGE = "click x button initial"
            const val SUCCESS_CLICK_CLOSE_PAGE = "click x button success"
            const val FAILED_CLICK_CLOSE_PAGE = "click x button failed"
        }
    }

    interface Label {
        companion object {
            const val EMONEY = "emoney"
            const val BRIZZI = "brizzi"
        }
    }

    interface Param {
        companion object {
            const val EVENT = "event"
            const val LOGGED_IN_STATUS = "IsLoggedInStatus"
            const val CURRENT_SITE = "currentSite"
            const val CLIENT_ID = "clientId"
            const val USER_ID = "userId"
            const val BUSINESS_UNIT = "businessUnit"
            const val SCREEN_NAME = "screenName"
            const val SESSION_IRIS = "sessionIris"

            const val CURRENT_SITE_EMONEY = "tokopediadigitalNFC"
            const val BUSINESS_UNIT_EMONEY = "top up and bills"
        }
    }
}