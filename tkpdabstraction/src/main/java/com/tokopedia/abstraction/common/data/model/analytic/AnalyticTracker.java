package com.tokopedia.abstraction.common.data.model.analytic;

import android.app.Activity;

/**
 * Created by nathan on 11/28/17.
 */

public interface AnalyticTracker {

    void sendEventTracking(String event, String category, String action, String label);

    void sendScreen(Activity activity, String screenName);

}
