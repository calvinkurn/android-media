package com.tokopedia.emoney

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class EmoneyAnalytics {

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

    fun onClickTopupEmoney(operatorName:String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_SALDO,
                Category.DIGITAL_HOMEPAGE,
                Action.CLICK_TOPUP + operatorName,
                operatorName
        ))
    }

    fun onClickTryAgainTapEmoney(operatorName:String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_SALDO,
                Category.DIGITAL_HOMEPAGE,
                Action.CLICK_TRY_AGAIN + operatorName,
                operatorName
        ))
    }

    fun onTapEmoneyCardShowLoading() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_NFC,
                Category.DIGITAL_NFC,
                Action.CHECK_STEP_2,
                Label.EMONEY
        ))
    }

    fun onShowErrorTracking() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_NFC,
                Category.DIGITAL_NFC,
                Action.FAILED_UPDATE_BALANCE,
                Label.EMONEY
        ))
    }

    fun onShowLastBalance() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_NFC,
                Category.DIGITAL_NFC,
                Action.SUCCESS_CHECK_BALANCE,
                Label.EMONEY
        ))
    }

    interface Event {
        companion object {
            val CLICK_NFC = "clickNFC"
            val CLICK_SALDO = "clickSaldo"
        }
    }

    interface Category {
        companion object {
            val DIGITAL_NFC = "digital - nfc"
            val DIGITAL_HOMEPAGE = "digital - homepage"
        }
    }

    interface Action {
        companion object {
            val CLICK_ACTIVATE = "click aktifkan"
            val CLICK_ACTIVATE_PROMPT = "click aktifkan_prompt"
            val CLICK_CANCEL_PROMPT = "click batalkan prompt"
            val CHECK_STEP_1 = "check step - 1"
            val CHECK_STEP_2 = "check step - 2"
            val CLICK_TOPUP = "click top up "
            val CLICK_TRY_AGAIN = "click coba lagi "
            val FAILED_UPDATE_BALANCE = "failed update saldo"
            val CARD_IS_NOT_SUPPORTED = "card not supported"
            val SUCCESS_CHECK_BALANCE = "success check saldo"
        }
    }

    interface Label {
        companion object {
            val EMONEY = "emoney"
            val BRIZZI = "brizzi"
        }
    }
}