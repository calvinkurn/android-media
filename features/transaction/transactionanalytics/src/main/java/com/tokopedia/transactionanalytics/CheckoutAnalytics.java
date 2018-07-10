package com.tokopedia.transactionanalytics;

import android.app.Activity;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

/**
 * @author anggaprasetiyo on 18/05/18.
 */
public abstract class CheckoutAnalytics {

    protected final AnalyticTracker analyticTracker;

    public CheckoutAnalytics(AnalyticTracker analyticTracker) {
        this.analyticTracker = analyticTracker;
    }

    public void sendScreenName(Activity activity, String screenName) {
        analyticTracker.sendScreen(activity, screenName);
    }
}
