package com.tokopedia.withdraw;

import android.app.Activity;


import javax.inject.Inject;

/**
 * @author : Steven 21/09/18
 */
public class WithdrawAnalytics {


    public static final String SCREEN_WITHDRAW = "/withdraw/";
    public static final String SCREEN_WITHDRAW_PASSWORD = "/withdraw-password/";

    private static final String EVENT_NAME_CLICK_SALDO = "clickSaldo";

    private static final String EVENT_CATEGORY_WITHDRAWAL_PAGE = "withdrawal page";

    private static final String EVENT_ACTION_CLICK_WITHDRAWAL = "click withdrawal";
    private static final String EVENT_ACTION_CLICK_WITHDRAWAL_ALL = "click withdrawal all";
    private static final String EVENT_ACTION_CLICK_BACK = "click back arrow";
    private static final String EVENT_ACTION_CLICK_ACCOUNT_BANK = "click account bank";
    private static final String EVENT_ACTION_CLICK_ACCOUNT_ADD = "click add account";
    private static final String EVENT_ACTION_CLICK_INFO = "click informasi penarikan saldo";
    private static final String EVENT_ACTION_CLICK_WITHDRAW_CONFIRM = "click withdrawal confirm";
    private static final String EVENT_ACTION_CLICK_X = "click x";
    private static final String EVENT_ACTION_CLICK_FORGOT = "click lupa kata sandi";
    private static final String EVENT_ACTION_CLICK_CLOSE_ERROR = "click close error message";

    @Inject
    public WithdrawAnalytics() {
    }

    public void sendScreen(Activity activity, String screenName) {
        analyticTracker.sendScreen(activity, screenName);
    }

    public void eventClickWithdrawal() {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_WITHDRAWAL,
                ""
        );
    }
    public void eventClickWithdrawalAll() {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_WITHDRAWAL_ALL,
                ""
        );
    }
    public void eventClickBackArrow() {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_BACK,
                ""
        );
    }
    public void eventClickAccountBank() {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_ACCOUNT_BANK,
                ""
        );
    }
    public void eventClickAddAccount() {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_ACCOUNT_ADD,
                ""
        );
    }
    public void eventClickInformasiPenarikanSaldo() {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_INFO,
                ""
        );
    }

    public void eventClickWithdrawalConfirm(String label) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_WITHDRAW_CONFIRM,
                label
        );
    }

    public void eventClickX() {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_X,
                ""
        );
    }
    public void eventClickForgotPassword() {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_FORGOT,
                ""
        );
    }
    public void eventClickCloseErrorMessage() {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_CLOSE_ERROR,
                ""
        );
    }

}
