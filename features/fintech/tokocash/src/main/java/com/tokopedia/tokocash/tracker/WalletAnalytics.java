package com.tokopedia.tokocash.tracker;


import javax.inject.Inject;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;

/**
 * Created by nabillasabbaha on 17/10/18.
 */
public class WalletAnalytics {

    private String GENERIC_EVENT = "clickSaldo";

    @Inject
    public WalletAnalytics() {
    }

    public void eventClickActivationOvoNow() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT, Category.TOKOCASH_TO_OVO,
                Action.CLICK_ACTIVATION_OVO_NOW, ""));
    }

    public void eventClickOvoLearnMore() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT, Category.TOKOCASH_TO_OVO,
                Action.CLICK_LEARN_MORE, ""));
    }

    public void eventClickTnc() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT, Category.TOKOCASH_TO_OVO,
                Action.CLICK_TNC, ""));
    }

    public void eventClickPopupPhoneNumber(String textButton) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT, Category.TOKOCASH_TO_OVO,
                Action.CLICK_PHONE_NUMBER + textButton, ""));
    }

    public void eventClickMakeNewOvoAccount() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT, Category.ACTIVATION_OVO,
                Action.CLICK_OVO_NEW_ACCOUNT, ""));
    }

    public void eventClickChangePhoneNumber() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT, Category.ACTIVATION_OVO,
                Action.CLICK_OVO_CHANGE_PHONE_NUMBER, ""));
    }

    public void eventClickActivationOvoHomepage() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT, Category.HOMEPAGE,
                Action.CLICK_HOME_ACTIVATION_OVO, ""));
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
