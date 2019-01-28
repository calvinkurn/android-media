package com.tokopedia.tokopoints.view.util;

import android.content.Context;

import java.util.List;
import java.util.Map;

public class AnalyticsTrackerUtil {
    public interface EventKeys {
        String EVENT_TOKOPOINT = "eventTokopoint";
        String TOKOPOINTS_LABEL = "eventTokopoint";
    }

    public interface CategoryKeys {
        String HOMEPAGE = "homepage-tokopoints";
    }

    public interface ActionKeys {
        String CLICK_POINT = "click point & tier status";
    }

    public static void sendEvent(Context context, String event, String category,
                                 String action, String label) {
    }

    public static void sendECommerceEvent(Context context, String event, String category,
                                          String action, String label, Map<String, Map<String, List<Map<String, String>>>> ecommerce) {
    }
}
