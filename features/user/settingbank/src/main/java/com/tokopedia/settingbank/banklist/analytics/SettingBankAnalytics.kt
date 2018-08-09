package com.tokopedia.settingbank.banklist.analytics

import android.app.Activity
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker
import java.io.File

/**
 * @author by nisie on 6/7/18.
 */
class SettingBankAnalytics(val tracker: AnalyticTracker) {

    companion object {
        fun createInstance(analyticTracker: AnalyticTracker): SettingBankAnalytics {
            return SettingBankAnalytics(analyticTracker)
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
        tracker.sendScreen(activity, screenName)
    }

    fun trackAddNewBankAccount() {
        tracker.sendEventTracking(
                Event.CLICK_BANK_ACCOUNT,
                Category.BANK_ACCOUNT_PAGE,
                "click add bank button",
                ""
        )
    }

    fun trackConfirmYesAddBankAccount() {
        tracker.sendEventTracking(
                Event.CLICK_CONFIRM,
                Category.BANK_ACCOUNT_PAGE,
                "click yes on add bank popup",
                ""
        )
    }

    fun trackEditBankAccount() {
        tracker.sendEventTracking(
                Event.CLICK_BANK_ACCOUNT,
                Category.BANK_ACCOUNT_PAGE,
                "click edit button",
                ""
        )
    }

    fun trackConfirmYesEditBankAccount() {
        tracker.sendEventTracking(
                Event.CLICK_CONFIRM,
                Category.BANK_ACCOUNT_PAGE,
                "click yes on edit popup",
                ""
        )
    }

    fun trackDeleteBankAccount() {
        tracker.sendEventTracking(
                Event.CLICK_BANK_ACCOUNT,
                Category.BANK_ACCOUNT_PAGE,
                "click delete button",
                ""
        )
    }

    fun trackConfirmYesDeleteBankAccount() {
        tracker.sendEventTracking(
                Event.CLICK_CONFIRM,
                Category.BANK_ACCOUNT_PAGE,
                "click yes on delete popup",
                ""
        )
    }

    fun trackSetDefaultAccount() {
        tracker.sendEventTracking(
                Event.CLICK_BANK_ACCOUNT,
                Category.BANK_ACCOUNT_PAGE,
                "click set as default button",
                ""
        )
    }

    fun trackConfirmYesSetDefaultAccount() {
        tracker.sendEventTracking(
                Event.CLICK_CONFIRM,
                Category.BANK_ACCOUNT_PAGE,
                "click yes on set as default popup",
                ""
        )
    }


}