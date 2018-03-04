package com.tokopedia.core.analytics;

import android.app.Activity;
import android.text.TextUtils;

import com.tokopedia.core.analytics.nishikino.model.Authenticated;
import com.tokopedia.core.util.SessionHandler;

public class ScreenTrackingBuilder {
    private static final String AF_UNAVAILABLE_VALUE = "none";

    private Authenticated authEvent;
    private Activity mActivity;
    private ScreenTracking.IOpenScreenAnalytics mOpenScreenAnalytics;

    public static ScreenTrackingBuilder newInstance(Activity activity,
                                                    ScreenTracking.IOpenScreenAnalytics openScreenAnalytics,
                                                    String afUniqueId) {
        return new ScreenTrackingBuilder(activity, openScreenAnalytics, afUniqueId);
    }

    private ScreenTrackingBuilder(Activity activity,
                                  ScreenTracking.IOpenScreenAnalytics openScreenAnalytics,
                                  String afUniqueId) {
        this.mOpenScreenAnalytics = openScreenAnalytics;
        this.mActivity = activity;
        authEvent = new Authenticated();

        authEvent.setUserFullName(SessionHandler.getLoginName(mActivity));
        authEvent.setUserID(SessionHandler.getGTMLoginID(mActivity));
        authEvent.setShopID(SessionHandler.getShopID(mActivity));
        authEvent.setUserSeller(SessionHandler.isUserHasShop(mActivity) ? 1 : 0);
        authEvent.setAfUniqueId(afUniqueId != null ? afUniqueId : AF_UNAVAILABLE_VALUE);
    }

    public ScreenTrackingBuilder setNetworkSpeed() {
        authEvent.setNetworkSpeed(TrackingUtils.getNetworkSpeed(mActivity));
        return this;
    }

    public ScreenTrackingBuilder setKeyCompetitorIntelligence(String data) {
        authEvent.setKeyCompetitorIntelligence(data);
        return this;
    }


    public void execute() {
        if (mOpenScreenAnalytics == null || TextUtils.isEmpty(mOpenScreenAnalytics.getScreenName())) {
            ScreenTracking.eventAuthScreen(authEvent, this.mActivity.getClass().getSimpleName());
        } else {
            ScreenTracking.eventAuthScreen(authEvent, mOpenScreenAnalytics.getScreenName());
        }
    }
}