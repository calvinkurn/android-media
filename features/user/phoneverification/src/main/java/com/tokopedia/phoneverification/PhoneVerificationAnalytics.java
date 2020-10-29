package com.tokopedia.phoneverification;

import android.content.Context;

import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;

/**
 * @author by nisie on 8/21/18.
 */
public class PhoneVerificationAnalytics {

    public static class Action{
        public static final String CLICK_VERIFY_NUMBER = "click verify number";

    }

    public static class Event {
        public static final String CLICK_APP_SHARE_REFERRAL = "clickReferral";;
    }

    public static class Category {
        public static final String REFERRAL = "Referral";

    }

    public static PhoneVerificationAnalytics createInstance() {
        return new PhoneVerificationAnalytics();
    }

    public void eventReferralAndShare(String action, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_APP_SHARE_REFERRAL,
                Category.REFERRAL,
                action,
                label
        ));
    }
}
