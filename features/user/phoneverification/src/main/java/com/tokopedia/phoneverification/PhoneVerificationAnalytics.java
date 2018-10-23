package com.tokopedia.phoneverification;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

/**
 * @author by nisie on 8/21/18.
 */
public class PhoneVerificationAnalytics {

    AnalyticTracker analyticTracker;

    public static class Action{
        public static final String CLICK_VERIFY_NUMBER = "click verify number";

    }

    public static class Event {
        public static final String CLICK_REGISTER = "clickRegister";
        public static final String CLICK_APP_SHARE_REFERRAL = "clickReferral";;
    }

    public static class Category {
        public static final String REGISTER_VERIFICATION_NUMBER = "register - verification phone " +
                "number";
        public static final String REFERRAL = "Referral";

    }


    public PhoneVerificationAnalytics(AnalyticTracker analyticTracker) {
        this.analyticTracker = analyticTracker;
    }

    public static PhoneVerificationAnalytics createInstance(Context context) {
        return new PhoneVerificationAnalytics(((AbstractionRouter) context).getAnalyticTracker());
    }


    public void eventClickOnBackPressed() {
        analyticTracker.sendEventTracking(
                Event.CLICK_REGISTER,
                Category.REGISTER_VERIFICATION_NUMBER,
                "click on back",
                ""
        );
    }

    public void eventClickVerifRegister() {
        analyticTracker.sendEventTracking(
                Event.CLICK_REGISTER,
                Category.REGISTER_VERIFICATION_NUMBER,
                "click on verifikasi",
                ""
        );
    }


    public void eventClickSkipRegister() {
        analyticTracker.sendEventTracking(
                Event.CLICK_REGISTER,
                Category.REGISTER_VERIFICATION_NUMBER,
                "click on lewati",
                ""
        );
    }

    public void eventClickChangePhoneRegister() {
        analyticTracker.sendEventTracking(
                Event.CLICK_REGISTER,
                Category.REGISTER_VERIFICATION_NUMBER,
                "click on ubah",
                ""
        );
    }

    public void eventClickRequestOtpSMSRegister() {
        analyticTracker.sendEventTracking(
                Event.CLICK_REGISTER,
                Category.REGISTER_VERIFICATION_NUMBER,
                "click on kirim sms verifikasi",
                ""
        );
    }

    public void eventReferralAndShare(String action, String label) {
        analyticTracker.sendEventTracking(
                Event.CLICK_APP_SHARE_REFERRAL,
                Category.REFERRAL,
                action,
                label
        );
    }
}
