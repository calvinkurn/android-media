package com.tokopedia.otp.common;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import javax.inject.Inject;

/**
 * @author by nisie on 1/26/18.
 */

public class OTPAnalytics {

    private final AnalyticTracker analyticTracker;

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

    @Inject
    public OTPAnalytics(AnalyticTracker analyticTracker) {
        this.analyticTracker = analyticTracker;
    }

    public void sendScreen(Activity activity, String screenName) {
        analyticTracker.sendScreen(activity, screenName);
    }
}
