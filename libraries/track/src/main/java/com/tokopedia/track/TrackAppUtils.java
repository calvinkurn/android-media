package com.tokopedia.track;

import java.util.HashMap;
import java.util.Map;

public class TrackAppUtils {
    public static final String EVENT = "event";
    public static final String EVENT_CATEGORY = "eventCategory";
    public static final String EVENT_ACTION = "eventAction";
    public static final String EVENT_LABEL = "eventLabel";

    public static Map<String, Object> gtmData(String event, String category, String action, String label) {
        Map<String, Object> map = new HashMap<>();
        map.put(EVENT, event);
        map.put(EVENT_CATEGORY, category);
        map.put(EVENT_ACTION, action);
        map.put(EVENT_LABEL, label);
        return map;
    }
}
