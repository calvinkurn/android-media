package com.tokopedia.loginphone.common.analytics;

import android.app.Activity;

import javax.inject.Inject;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;

/**
 * @author by nisie on 1/5/18.
 */

public class LoginPhoneNumberAnalytics {

    @Inject
    public LoginPhoneNumberAnalytics() {
    }

    public static class Screen {
        public static final String SCREEN_LOGIN_PHONE_NUMBER = "Login by Phone Number";
        public static final String SCREEN_CHOOSE_TOKOCASH_ACCOUNT = "choose account";
        public static final String SCREEN_NOT_CONNECTED_TO_TOKOCASH = "Login Tokocash - Not Connected";
        public static final String SCREEN_REGISTER_WITH_PHONE_NUMBER = "Register with Phone Number Page";
    }

    public static class Event {
        static final String EVENT_CLICK_LOGIN = "clickLogin";
    }

    public static class Category {
        static final String LOGIN_WITH_PHONE = "login with phone";
        static final String PHONE_VERIFICATION = "phone verification";
    }

    public static class Action {

        static final String CLICK_ON_NEXT = "click on selanjutnya";
        static final String CLICK_ON_VERIFICATION = "click on verifikasi";
        static final String CLICK_ON_RESEND_VERIFICATION = "click on kirim ulang";
        static final String LOGIN_SUCCESS = "login success";
        public static final String CHANGE_METHOD = "change method";

    }

    public static class Label {

        static final String SMS = "sms";
        static final String PHONE = "phone";
        static final String TOKOCASH = "Tokocash";
    }

    public void sendScreen(Activity activity, String screenName) {
        TrackApp.getInstance().getGTM().sendScreenAuthenticated(screenName);
    }

    public void trackLoginWithPhone() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_LOGIN,
                Category.LOGIN_WITH_PHONE,
                Action.CLICK_ON_NEXT,
                ""
        ));
    }
    public void eventSuccessLoginPhoneNumber() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_LOGIN,
                Category.LOGIN_WITH_PHONE,
                Action.LOGIN_SUCCESS,
                Label.TOKOCASH));
    }


    public void trackVerifyOtpClick(String mode) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_LOGIN,
                Category.PHONE_VERIFICATION,
                Action.CLICK_ON_VERIFICATION,
                mode
        ));
    }

    public void trackResendVerification(String mode) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_LOGIN,
                Category.PHONE_VERIFICATION,
                Action.CLICK_ON_RESEND_VERIFICATION,
                mode
        ));
    }

    public void eventChooseVerificationMethodTracking(String modeName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_LOGIN,
                Category.PHONE_VERIFICATION,
                Action.CHANGE_METHOD,
                modeName
        ));
    }

}
