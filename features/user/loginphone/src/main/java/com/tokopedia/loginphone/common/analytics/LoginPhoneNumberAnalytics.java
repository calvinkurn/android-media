package com.tokopedia.loginphone.common.analytics;

import android.app.Activity;
import android.os.Build;

import javax.inject.Inject;

import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

import java.util.HashMap;
import java.util.Map;


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
        public static final String CLICK_REGISTER = "clickRegister";
    }

    public static class Category {
        static final String LOGIN_WITH_PHONE = "login with phone";
        static final String PHONE_VERIFICATION = "phone verification";
        static final String REGISTER_PAGE = "register page";
    }

    public static class Action {

        static final String CLICK_ON_NEXT = "click on selanjutnya";
        static final String CLICK_ON_VERIFICATION = "click on verifikasi";
        static final String CLICK_ON_RESEND_VERIFICATION = "click on kirim ulang";
        static final String LOGIN_SUCCESS = "login success";
        static final String CLICK_ON_BUTTON_DAFTAR_PHONE = "click on button daftar - phone number";
        static final String CLICK_ON_BUTTON_FACEBOOK = "click on button facebook";
        public static final String CHANGE_METHOD = "change method";

    }

    public static class Label {

        static final String SMS = "sms";
        static final String PHONE = "phone";
        static final String TOKOCASH = "Tokocash";
        static final String LOGIN_SUCCESS = "login success";
        static final String REGISTER_SUCCESS = "register success";
    }

    public void sendScreen(Activity activity, String screenName) {
        String screenNameMessage = screenName + " | " + Build.FINGERPRINT + " | " + Build.MANUFACTURER + " | "
                + Build.BRAND + " | " + Build.DEVICE + " | " + Build.PRODUCT + " | " + Build.MODEL
                + " | " + Build.TAGS;
        Map<String, String> screenNameMap = new HashMap<>();
        screenNameMap.put("screenName", screenNameMessage);
        ServerLogger.log(Priority.P2, "FINGERPRINT", screenNameMap);
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

    public void eventSuccessLoginPhoneNumberSmartRegister() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_REGISTER,
                Category.REGISTER_PAGE,
                Action.CLICK_ON_BUTTON_DAFTAR_PHONE,
                Label.LOGIN_SUCCESS));
    }

    public void eventSuccessFbPhoneNumber() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_REGISTER,
                Category.REGISTER_PAGE,
                Action.CLICK_ON_BUTTON_FACEBOOK,
                Label.REGISTER_SUCCESS));
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

    public void eventClickBackRegisterVerificationPage() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_REGISTER,
                "register with phone number otp",
                "click on button back",
                ""
        ));
    }
}
