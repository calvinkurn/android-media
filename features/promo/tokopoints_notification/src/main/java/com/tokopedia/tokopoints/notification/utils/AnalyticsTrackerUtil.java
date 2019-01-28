package com.tokopedia.tokopoints.notification.utils;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

public class AnalyticsTrackerUtil {
    public interface EventKeys {
        String EVENT = "event";
        String EVENT_CATEGORY = "eventCategory";
        String EVENT_ACTION = "eventAction";
        String EVENT_LABEL = "eventLabel";
        String ECOMMERCE = "ecommerce";
        String EVENT_CLICK_COUPON = "clickCoupon";
    }

    public interface CategoryKeys {
        String POPUP_TERIMA_HADIAH = "pop up terima hadiah kupon";
    }

    public interface ActionKeys {

        String CLICK_CLOSE_BUTTON = "click close button";
        String CLICK_GUNAKAN_KUPON = "click gunakan kupon";
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
}
