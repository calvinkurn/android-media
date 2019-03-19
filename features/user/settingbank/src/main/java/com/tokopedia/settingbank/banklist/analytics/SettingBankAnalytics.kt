package com.tokopedia.settingbank.banklist.analytics

import android.app.Activity
import java.io.File
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;

/**
 * @author by nisie on 6/7/18.
 */
class SettingBankAnalytics() {

    companion object {
        fun createInstance(): SettingBankAnalytics {
            return SettingBankAnalytics()
        }

        val SCREEN_NAME: String = "Bank Account Page"
        val SCREEN_NAME_ADD: String = "Add Bank Page"
        val SCREEN_NAME_EDIT: String = "Edit Bank Page"
        val SCREEN_NAME_CHOOSE_BANK: String = "Choose Bank Page"
    }

    class Event {
        companion object {
            const val CLICK_BANK_ACCOUNT: String = "clickBankAccount"
            const val CLICK_CONFIRM: String = "clickConfirm"

        }
    }

    class Category {
        companion object {
            const val BANK_ACCOUNT_PAGE: String = "bank account page"
        }
    }

    fun sendScreen(activity: Activity, screenName: String) {
        TrackApp.getInstance()?.gtm?.sendScreenAuthenticated( screenName)
    }

    fun trackAddNewBankAccount() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_BANK_ACCOUNT,
                Category.BANK_ACCOUNT_PAGE,
                "click add bank button",
                ""
        ))
    }

    fun trackConfirmYesAddBankAccount() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_CONFIRM,
                Category.BANK_ACCOUNT_PAGE,
                "click yes on add bank popup",
                ""
        ))
    }

    fun trackEditBankAccount() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_BANK_ACCOUNT,
                Category.BANK_ACCOUNT_PAGE,
                "click edit button",
                ""
        ))
    }

    fun trackConfirmYesEditBankAccount() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_CONFIRM,
                Category.BANK_ACCOUNT_PAGE,
                "click yes on edit popup",
                ""
        ))
    }

    fun trackDeleteBankAccount() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_BANK_ACCOUNT,
                Category.BANK_ACCOUNT_PAGE,
                "click delete button",
                ""
        ))
    }

    fun trackConfirmYesDeleteBankAccount() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_CONFIRM,
                Category.BANK_ACCOUNT_PAGE,
                "click yes on delete popup",
                ""
        ))
    }

    fun trackSetDefaultAccount() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_BANK_ACCOUNT,
                Category.BANK_ACCOUNT_PAGE,
                "click set as default button",
                ""
        ))
    }

    fun trackConfirmYesSetDefaultAccount() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_CONFIRM,
                Category.BANK_ACCOUNT_PAGE,
                "click yes on set as default popup",
                ""
        ))
    }


}