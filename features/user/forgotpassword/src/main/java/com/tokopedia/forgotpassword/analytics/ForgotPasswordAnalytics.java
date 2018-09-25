package com.tokopedia.forgotpassword.analytics;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

/**
 * @author by nisie on 8/8/18.
 */
public class ForgotPasswordAnalytics {

    private AnalyticTracker analyticTracker;

    public ForgotPasswordAnalytics(AnalyticTracker analyticTracker) {
        this.analyticTracker = analyticTracker;
    }

    public void trackSuccessForgotPassword() {

        analyticTracker.sendEventTracking(
                "passwordForget",
                "Forgot Password",
                "Reset Success",
                "Reset Password"
        );
    }

    public static class Screen {
        public static final String FORGOT_PASSWORD = "Forgot password page";
    }
}
