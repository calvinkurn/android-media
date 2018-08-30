package com.tokopedia.tokopoints.view.util;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import java.util.HashMap;

public class AnalyticsTrackerUtil {
    interface EventKeys {
        String EVENT = "event";
        String EVENT_CATEGORY = "eventCategory";
        String EVENT_ACTION = "eventAction";
        String EVENT_LABEL = "eventLabel";
        String ECOMMERCE = "ecommerce";
    }

    public static void sendEvent(Context context, String event, String category,
                                 String action, String label) {
        if (context == null) {
            return;
        }

        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        if (tracker == null) {
            return;
        }

        tracker.sendEventTracking(event, category, action, label);
    }

    public void sendECommerceEvent(Context context, String event, String category,
                                   String action, String label, HashMap<String, Object> ecommerce) {
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        if (tracker == null)
            return;
        HashMap<String, Object> map = new HashMap<>();
        map.put(EventKeys.EVENT, event);
        map.put(EventKeys.EVENT_CATEGORY, category);
        map.put(EventKeys.EVENT_ACTION, action);
        map.put(EventKeys.EVENT_LABEL, label);
        map.put(EventKeys.ECOMMERCE, ecommerce);
        tracker.sendEnhancedEcommerce(map);
    }
}
