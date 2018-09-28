package com.tokopedia.core.analytics;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.core.analytics.nishikino.model.Authenticated;
import com.tokopedia.core.deprecated.SessionHandler;

public class ScreenTrackingBuilder {
    private static final String AF_UNAVAILABLE_VALUE = "none";
    private final SessionHandler sessionHandler;

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
        sessionHandler = new SessionHandler(activity);

        authEvent.setUserFullName(sessionHandler.getLoginName());
        authEvent.setUserID(sessionHandler.getGTMLoginID());
        authEvent.setShopID(sessionHandler.getShopID());
        authEvent.setUserSeller(sessionHandler.isUserHasShop() ? 1 : 0);
        authEvent.setAfUniqueId(afUniqueId != null ? afUniqueId : AF_UNAVAILABLE_VALUE);
    }

    public ScreenTrackingBuilder setNetworkSpeed(String networkSpeed) {
        authEvent.setNetworkSpeed(networkSpeed);
        return this;
    }

    public ScreenTrackingBuilder setKeyCompetitorIntelligence(String data) {
        if (!TextUtils.isEmpty(data))
            authEvent.setKeyCompetitorIntelligence(data);
        return this;
    }


    public void execute(Context context) {
        if (mOpenScreenAnalytics == null || TextUtils.isEmpty(mOpenScreenAnalytics.getScreenName())) {
            ScreenTracking.eventAuthScreen(context, authEvent, this.mActivity.getClass().getSimpleName());
        } else {
            ScreenTracking.eventAuthScreen(context, authEvent, mOpenScreenAnalytics.getScreenName());
        }
    }
}