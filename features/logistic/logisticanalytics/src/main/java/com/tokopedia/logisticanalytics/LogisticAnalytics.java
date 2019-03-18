package com.tokopedia.logisticanalytics;

import android.app.Activity;

import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

import java.util.Map;

public abstract class LogisticAnalytics {


    public LogisticAnalytics() {
    }

    public void sendScreenName(Activity activity, String screenName) {
        TrackApp.getInstance().getGTM().sendScreenAuthenticated(screenName);
    }

    void sendEventCategoryActionLabel(String event, String eventCategory,
                                      String eventAction, String eventLabel) {

        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(event, eventCategory, eventAction, eventLabel));
    }


    void sendEventCategoryAction(String event, String eventCategory,
                                 String eventAction) {
        sendEventCategoryActionLabel(event, eventCategory, eventAction, "");
    }


    void sendEnhancedEcommerce(Map<String, Object> dataLayer) {
        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(dataLayer);
    }
}
