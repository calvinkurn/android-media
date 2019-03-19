package com.tokopedia.otp.common;


import android.app.Activity;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;

import javax.inject.Inject;

/**
 * @author by nisie on 1/26/18.
 */

public class OTPAnalytics {

    public static class Screen {
        public static final String SCREEN_COTP_SMS = "Input OTP sms";
        public static final String SCREEN_COTP_CALL = "Input OTP call";
        public static final String SCREEN_COTP_EMAIL = "Input OTP email";

        public static final String SCREEN_SELECT_VERIFICATION_METHOD = "change method";
        public static final String SCREEN_COTP_DEFAULT = "Input OTP";

        public static final String SCREEN_INTERRUPT_VERIFICATION_DEFAULT = "Verification Interrupt Page";
        public static final String SCREEN_INTERRUPT_VERIFICATION_SMS = "Verification Interrupt Sms";
        public static final String SCREEN_INTERRUPT_VERIFICATION_EMAIL = "Verification Interrupt Email";
        public static final String SCREEN_PHONE_NUMBER_VERIFICATION = "Phone number verification Page";

    }

    public static class Event {
        public static final String CLICK_LOGIN = "clickLogin";
        public static final String CLICK_CONFIRM = "clickConfirm";
        public static final String CLICK_BACK = "clickBack";
        public static final String CLICK_REGISTER = "clickRegister";
        public static final String CLICK_HOME_PAGE = "clickHomePage";
        public static final String CLICK_USER_PROFILE = "clickUserProfile";
        public static final String CLICK_OTP = "clickOtp";
    }

    public static class Action {
        public static final String INPUT_OTP_PAGE = "input otp page";
        public static final String CHOOSE_OTP_PAGE = "choose otp page";
    }

    @Inject
    public OTPAnalytics() {

    }

    public void sendScreen(Activity activity, String screenName) {
        TrackApp.getInstance().getGTM().sendScreenAuthenticated(screenName);
    }

    public void eventClickBackOTPPage(int otpType) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_BACK,
                Action.INPUT_OTP_PAGE,
                "click back button",
                String.valueOf(otpType)
        ));
    }

    public void eventClickVerifyButton(int otpType) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_CONFIRM,
                Action.INPUT_OTP_PAGE,
                "click on verifikasi",
                String.valueOf(otpType)

        ));
    }

    public void eventClickResendOtp(int otpType) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_OTP,
                Action.INPUT_OTP_PAGE,
                "click on kirim ulang",
                String.valueOf(otpType)

        ));
    }

    public void eventClickUseOtherMethod(int otpType) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_OTP,
                Action.INPUT_OTP_PAGE,
                "click on gunakan metode verifikasi lain",
                String.valueOf(otpType)
        ));
    }


    public void eventClickMethodOtp(int otpType, String modeName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_OTP,
                Action.CHOOSE_OTP_PAGE,
                "click on otp method ",
                String.format("%s - %s", String.valueOf(otpType), modeName)
        ));
    }
}
