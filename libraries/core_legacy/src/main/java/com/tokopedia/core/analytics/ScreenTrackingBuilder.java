package com.tokopedia.core.analytics;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.core.analytics.nishikino.model.Authenticated;
import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.utils.RouterUtils;

public class ScreenTrackingBuilder {
    private static final String AF_UNAVAILABLE_VALUE = "none";

    private Authenticated authEvent;
    private String screenName;

    public static ScreenTrackingBuilder newInstance(Context context,
                                                    ScreenTracking.IOpenScreenAnalytics openScreenAnalytics,
                                                    String afUniqueId) {
        return new ScreenTrackingBuilder(context, openScreenAnalytics, afUniqueId);
    }

    public static ScreenTrackingBuilder newInstance(Context context,
                                                    String screenName,
                                                    String afUniqueId) {
        return new ScreenTrackingBuilder(context, screenName, afUniqueId);
    }

    private ScreenTrackingBuilder(Context context,
                                  ScreenTracking.IOpenScreenAnalytics openScreenAnalytics,
                                  String afUniqueId) {
        if (openScreenAnalytics!= null) {
            screenName = openScreenAnalytics.getScreenName();
        }
        initAuthEvent(context, afUniqueId);
    }

    private ScreenTrackingBuilder(Context context,
                                  String screenName,
                                  String afUniqueId) {
        this.screenName = screenName;
        initAuthEvent(context, afUniqueId);
    }

    private void initAuthEvent(Context context, String afUniqueId){
        authEvent = new Authenticated();
        SessionHandler sessionHandler = RouterUtils.getRouterFromContext(context).legacySessionHandler();

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

    public ScreenTrackingBuilder setDeepLinkUrl(String deepLinkUrlStr){
        if(!TextUtils.isEmpty(deepLinkUrlStr))
            authEvent.setDeepLinkUrl(deepLinkUrlStr);
        return this;
    }

    public void execute(Context context) {
        if (!TextUtils.isEmpty(screenName)) {
            ScreenTracking.eventAuthScreen(context, authEvent, screenName);
        }
    }

    public void sendAuth(Context context) {
        ScreenTracking.eventAuth(context, authEvent);
    }
}