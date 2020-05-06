package com.tokopedia.withdraw

import android.app.Activity
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

/**
 * @author : Steven 21/09/18
 */
class WithdrawAnalytics @Inject constructor() {
    fun sendScreen(activity: Activity?, screenName: String?) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    fun eventClickWithdrawal() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_WITHDRAWAL,
                ""
        ))
    }

    fun eventClickWithdrawalAll() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_WITHDRAWAL_ALL,
                ""
        ))
    }

    fun eventClickBackArrow() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_BACK,
                ""
        ))
    }

    fun eventClickTarikSaldo() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_TARIK_SALDO,
                ""
        ))
    }

    fun eventClickJoinNow() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_JOIN_NOW,
                ""
        ))
    }

    fun eventClickInfo() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_WITHDRAW_PWM,
                ""
        ))
    }

    fun eventClickGotoDashboard() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_CHECK_DASHBOARD,
                ""
        ))
    }

    fun eventClickContinueBtn() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_CONTINUE,
                ""
        ))
    }

    fun eventClickTANDC() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_TANDC,
                ""
        ))
    }

    fun eventClickAccountBank() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_ACCOUNT_BANK,
                ""
        ))
    }

    fun eventClickAddAccount() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_ACCOUNT_ADD,
                ""
        ))
    }

    fun eventClickBackToSaldoPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_BACK_TO_SALDO_DETAIL,
                ""
        ))
    }

    fun eventClicGoToHomePage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_BACK_TO_HOME_PAGE,
                ""
        ))
    }

    fun eventClickWithdrawalConfirm(label: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_WITHDRAW_CONFIRM,
                label
        ))
    }

    fun eventClickX() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_X,
                ""
        ))
    }

    fun eventClickForgotPassword() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_FORGOT,
                ""
        ))
    }

    fun eventClickCloseErrorMessage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_CLOSE_ERROR,
                ""
        ))
    }

    fun eventOnPremiumProgramWidgetClick() {
        TrackApp.getInstance().gtm.pushEvent(EVENT_NAME_PREMIUM_PROGRAM, TrackAppUtils.gtmData(EVENT_NAME_PREMIUM_PROGRAM,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_PREMIUM_PROGRAM,
                ""
        ))
    }

    companion object {
        const val SCREEN_WITHDRAW = "/withdraw/"
        const val SCREEN_WITHDRAW_PASSWORD = "/withdraw-password/"
        const val SCREEN_WITHDRAW_SUCCESS_PAGE = "/penarikan-diproses/"
        private const val EVENT_NAME_CLICK_SALDO = "clickWithdrawal"
        private const val EVENT_CATEGORY_WITHDRAWAL_PAGE = "withdrawal page"
        private const val EVENT_ACTION_CLICK_WITHDRAWAL = "click withdrawal"
        private const val EVENT_ACTION_CLICK_WITHDRAWAL_ALL = "click withdrawal all"
        private const val EVENT_ACTION_CLICK_BACK = "click back arrow"
        private const val EVENT_ACTION_CLICK_ACCOUNT_BANK = "click account bank"
        private const val EVENT_ACTION_CLICK_ACCOUNT_ADD = "click tambah rekening"
        private const val EVENT_ACTION_CLICK_INFO = "click informasi penarikan saldo"
        private const val EVENT_ACTION_CLICK_WITHDRAW_CONFIRM = "click withdrawal confirm"
        private const val EVENT_ACTION_CLICK_X = "click x"
        private const val EVENT_ACTION_CLICK_FORGOT = "click lupa kata sandi"
        private const val EVENT_ACTION_CLICK_CLOSE_ERROR = "click close error message"
        private const val EVENT_ACTION_CLICK_TARIK_SALDO = "click tarik saldo"
        private const val EVENT_ACTION_CLICK_TANDC = "click ketentuan penarikan saldo"
        private const val EVENT_ACTION_CLICK_JOIN_NOW = "click gabung sekarang"
        private const val EVENT_ACTION_CLICK_CONTINUE = "click lanjut tarik"
        private const val EVENT_ACTION_CLICK_WITHDRAW_PWM = "click withdrawal power merchant"
        private const val EVENT_ACTION_CLICK_CHECK_DASHBOARD = "click lihat dashboard"
        private const val EVENT_ACTION_CLICK_BACK_TO_SALDO_DETAIL = "click kembali detail saldo"
        private const val EVENT_ACTION_CLICK_BACK_TO_HOME_PAGE = "click belanja tokopedia"
        private const val EVENT_NAME_PREMIUM_PROGRAM = "viewWithdrawalIris"
        private const val EVENT_ACTION_PREMIUM_PROGRAM = "view ticker rekening premium"
    }
}