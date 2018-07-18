package com.tokopedia.transactionanalytics;

import android.app.Activity;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import java.util.Map;

/**
 * @author anggaprasetiyo on 18/05/18.
 */
public abstract class TransactionAnalytics {

    protected final AnalyticTracker analyticTracker;

    TransactionAnalytics(AnalyticTracker analyticTracker) {
        this.analyticTracker = analyticTracker;
    }

    public void sendScreenName(Activity activity, String screenName) {
        if (analyticTracker != null) analyticTracker.sendScreen(activity, screenName);
    }

    protected void sendEventCategoryActionLabel(String event, String eventCategory,
                                                String eventAction, String eventLabel) {
        if (analyticTracker != null)
            analyticTracker.sendEventTracking(event, eventCategory, eventAction, eventLabel);
    }

    protected void sendEventCategoryAction(String event, String eventCategory,
                                           String eventAction) {
        sendEventCategoryActionLabel(event, eventCategory, eventAction, "");
    }

    protected void sendEnhancedEcommerce(Map<String, Object> dataLayer) {
        if (analyticTracker != null)
            analyticTracker.sendEnhancedEcommerce(dataLayer);
    }
}
