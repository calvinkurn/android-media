package com.tokopedia.logisticanalytics;

import android.app.Activity;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import java.util.Map;

public abstract class LogisticAnalytics {

    private final AnalyticTracker analyticTracker;

    public LogisticAnalytics(AnalyticTracker analyticTracker) {
        this.analyticTracker = analyticTracker;
    }

    public void sendScreenName(Activity activity, String screenName) {
        if (analyticTracker != null) analyticTracker.sendScreen(activity, screenName);
    }

    void sendEventCategoryActionLabel(String event, String eventCategory,
                                      String eventAction, String eventLabel) {
        if (analyticTracker != null)
            analyticTracker.sendEventTracking(event, eventCategory, eventAction, eventLabel);
    }

    void sendEventCategoryAction(String event, String eventCategory,
                                 String eventAction) {
        sendEventCategoryActionLabel(event, eventCategory, eventAction, "");
    }

    void sendEnhancedEcommerce(Map<String, Object> dataLayer) {
        if (analyticTracker != null) {
            analyticTracker.sendEnhancedEcommerce(dataLayer);
        }
    }
}
