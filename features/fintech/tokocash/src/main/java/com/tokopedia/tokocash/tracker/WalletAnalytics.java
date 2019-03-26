package com.tokopedia.tokocash.tracker;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 17/10/18.
 */
public class WalletAnalytics {

    private String GENERIC_EVENT = "clickSaldo";
    private String HOME_PAGE = "clickHomepage";

    private AnalyticTracker analyticTracker;

    @Inject
    public WalletAnalytics(AnalyticTracker analyticTracker) {
        this.analyticTracker = analyticTracker;
    }

    public void eventClickActivationOvoNow() {
        analyticTracker.sendEventTracking(GENERIC_EVENT, Category.TOKOCASH_TO_OVO,
                Action.CLICK_ACTIVATION_OVO_NOW, "");
    }

    public void eventClickOvoLearnMore() {
        analyticTracker.sendEventTracking(GENERIC_EVENT, Category.TOKOCASH_TO_OVO,
                Action.CLICK_LEARN_MORE, "");
    }

    public void eventClickTnc() {
        analyticTracker.sendEventTracking(GENERIC_EVENT, Category.TOKOCASH_TO_OVO,
                Action.CLICK_TNC, "");
    }

    public void eventClickPopupPhoneNumber(String textButton) {
        analyticTracker.sendEventTracking(GENERIC_EVENT, Category.TOKOCASH_TO_OVO,
                Action.CLICK_PHONE_NUMBER + textButton, "");
    }

    public void eventClickMakeNewOvoAccount() {
        analyticTracker.sendEventTracking(GENERIC_EVENT, Category.ACTIVATION_OVO,
                Action.CLICK_OVO_NEW_ACCOUNT, "");
    }

    public void eventClickChangePhoneNumber() {
        analyticTracker.sendEventTracking(GENERIC_EVENT, Category.ACTIVATION_OVO,
                Action.CLICK_OVO_CHANGE_PHONE_NUMBER, "");
    }

    public void eventClickActivationOvoHomepage() {
        analyticTracker.sendEventTracking(HOME_PAGE, Category.HOMEPAGE,
                Action.CLICK_HOME_ACTIVATION_OVO, "");
    }

    private static class Category {
        static String TOKOCASH_TO_OVO = "tokocash to ovo";
        static String ACTIVATION_OVO = "aktivasi ovo baru";
        static String HOMEPAGE = "homepage";
    }

    private static class Action {
        static String CLICK_ACTIVATION_OVO_NOW = "click aktifkan ovo sekarang";
        static String CLICK_LEARN_MORE = "click pelajari lebih lanjut";
        static String CLICK_TNC = "click lihat syarat & ketentuan";
        static String CLICK_PHONE_NUMBER = "click ";
        static String CLICK_OVO_NEW_ACCOUNT = "click buat akun ovo baru";
        static String CLICK_OVO_CHANGE_PHONE_NUMBER = "click ubah nomor ponsel sekarang";
        static String CLICK_HOME_ACTIVATION_OVO = "click aktivasi ovo pada homepage";
    }
}
