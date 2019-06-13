package com.tokopedia.core.analytics;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.core.analytics.nishikino.model.Authenticated;

import java.util.HashMap;
import java.util.Map;

public class ScreenTrackingBuilder {
    private String screenName;
    private Map<String, String> customDimension = new HashMap<>();

    public static ScreenTrackingBuilder newInstance(ScreenTracking.IOpenScreenAnalytics openScreenAnalytics) {
        return new ScreenTrackingBuilder(openScreenAnalytics);
    }

    public static ScreenTrackingBuilder newInstance(String screenName) {
        return new ScreenTrackingBuilder(screenName);
    }

    private ScreenTrackingBuilder(ScreenTracking.IOpenScreenAnalytics openScreenAnalytics) {
        if (openScreenAnalytics != null) {
            screenName = openScreenAnalytics.getScreenName();
        }
    }

    private ScreenTrackingBuilder(String screenName) {
        this.screenName = screenName;
    }

    public ScreenTrackingBuilder setNetworkSpeed(String networkSpeed) {
        customDimension.put(Authenticated.KEY_NETWORK_SPEED, networkSpeed);
        return this;
    }

    public ScreenTrackingBuilder setKeyCompetitorIntelligence(String data) {
        if (!TextUtils.isEmpty(data)) {
            customDimension.put(Authenticated.KEY_COMPETITOR_INTELLIGENCE, data);
        }
        return this;
    }

    public void execute(Context context) {
        if (!TextUtils.isEmpty(screenName)) {
            ScreenTracking.eventAuthScreen(context, customDimension, screenName);
        }
    }

}