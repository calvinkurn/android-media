package com.tokopedia.withdraw;

import android.app.Activity;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;

import javax.inject.Inject;

/**
 * @author : Steven 21/09/18
 */
public class WithdrawAnalytics {


    public static final String SCREEN_WITHDRAW = "/withdraw/";
    public static final String SCREEN_WITHDRAW_PASSWORD = "/withdraw-password/";
    public static final String SCREEN_WITHDRAW_SUCCESS_PAGE = "/penarikan-diproses/";

    private static final String EVENT_NAME_CLICK_SALDO = "clickWithdrawal";
    private static final String EVENT_CATEGORY_WITHDRAWAL_PAGE = "withdrawal page";

    private static final String EVENT_ACTION_CLICK_WITHDRAWAL = "click withdrawal";
    private static final String EVENT_ACTION_CLICK_WITHDRAWAL_ALL = "click withdrawal all";
    private static final String EVENT_ACTION_CLICK_BACK = "click back arrow";
    private static final String EVENT_ACTION_CLICK_ACCOUNT_BANK = "click account bank";
    private static final String EVENT_ACTION_CLICK_ACCOUNT_ADD = "click tambah rekening";
    private static final String EVENT_ACTION_CLICK_INFO = "click informasi penarikan saldo";
    private static final String EVENT_ACTION_CLICK_WITHDRAW_CONFIRM = "click withdrawal confirm";
    private static final String EVENT_ACTION_CLICK_X = "click x";
    private static final String EVENT_ACTION_CLICK_FORGOT = "click lupa kata sandi";
    private static final String EVENT_ACTION_CLICK_CLOSE_ERROR = "click close error message";
    private static final String EVENT_ACTION_CLICK_TARIK_SALDO = "click tarik saldo";
    private static final String EVENT_ACTION_CLICK_TANDC = "click ketentuan penarikan saldo";
    private static final String EVENT_ACTION_CLICK_JOIN_NOW = "click gabung sekarang";
    private static final String EVENT_ACTION_CLICK_CONTINUE = "click lanjut tarik";
    private static final String EVENT_ACTION_CLICK_WITHDRAW_PWM = "click withdrawal power merchant";
    private static final String EVENT_ACTION_CLICK_CHECK_DASHBOARD = "click lihat dashboard";
    private static final String EVENT_ACTION_CLICK_BACK_TO_SALDO_DETAIL = "click kembali detail saldo";
    private static final String EVENT_ACTION_CLICK_BACK_TO_HOME_PAGE = "click belanja tokopedia";

    @Inject
    public WithdrawAnalytics() {
    }

    public void sendScreen(Activity activity, String screenName) {
        TrackApp.getInstance().getGTM().sendScreenAuthenticated( screenName);
    }

    public void eventClickWithdrawal() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_WITHDRAWAL,
                ""
        ));
    }
    public void eventClickWithdrawalAll() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_WITHDRAWAL_ALL,
                ""
        ));
    }
    public void eventClickBackArrow() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_BACK,
                ""
        ));
    }
    public void eventClickTarikSaldo() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_TARIK_SALDO,
                ""
        ));
    }
    public void eventClickJoinNow() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_JOIN_NOW,
                ""
        ));
    }
    public void eventClickInfo() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_WITHDRAW_PWM,
                ""
        ));
    }
    public void eventClickGotoDashboard() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_CHECK_DASHBOARD,
                ""
        ));
    }
    public void eventClickContinueBtn() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_CONTINUE,
                ""
        ));
    }
    public void eventClickTANDC() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_TANDC,
                ""
        ));
    }
    public void eventClickAccountBank() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_ACCOUNT_BANK,
                ""
        ));
    }
    public void eventClickAddAccount() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_ACCOUNT_ADD,
                ""
        ));
    }
    public void eventClickBackToSaldoPage() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_BACK_TO_SALDO_DETAIL,
                ""
        ));
    }
    public void eventClicGoToHomePage() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_BACK_TO_HOME_PAGE,
                ""
        ));
    }

    public void eventClickWithdrawalConfirm(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_WITHDRAW_CONFIRM,
                label
        ));
    }

    public void eventClickX() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_X,
                ""
        ));
    }
    public void eventClickForgotPassword() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_FORGOT,
                ""
        ));
    }
    public void eventClickCloseErrorMessage() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_CLOSE_ERROR,
                ""
        ));
    }

}
