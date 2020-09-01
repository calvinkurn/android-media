package com.tokopedia.core.analytics.appsflyer;

import com.appsflyer.AFInAppEventType;

import java.util.Map;

public class AppsflyerEventValidation {
    public static void validateData(String eventName, Map<String, Object> eventValue){
        switch (eventName){
            case AFInAppEventType.PURCHASE:
                break;
            case "criteo_track_transaction":
                break;
            case AFInAppEventType.ADD_TO_CART:
                break;

        }
    }
}
