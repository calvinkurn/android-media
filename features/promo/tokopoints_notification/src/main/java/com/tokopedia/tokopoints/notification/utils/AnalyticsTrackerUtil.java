package com.tokopedia.tokopoints.notification.utils;

import android.content.Context;

import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

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
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(event, category, action, label));
    }
}
