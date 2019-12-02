package com.tokopedia.purchase_platform.common.analytics;

import android.app.Activity;
import android.os.Bundle;

import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;

import java.util.Map;

/**
 * @author anggaprasetiyo on 18/05/18.
 */
public abstract class TransactionAnalytics {

    protected TransactionAnalytics() {
    }

    Analytics getTracker() {
        return TrackApp.getInstance().getGTM();
    }

    public void sendScreenName(Activity activity, String screenName) {
        TrackApp.getInstance().getGTM().sendScreenAuthenticated(screenName);
    }

    protected void sendEventCategoryActionLabel(String event, String eventCategory,
                                      String eventAction, String eventLabel) {

        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                event, eventCategory, eventAction, eventLabel));
    }


    protected void sendEventCategoryAction(String event, String eventCategory,
                                 String eventAction) {
        sendEventCategoryActionLabel(event, eventCategory, eventAction, "");
    }


    protected void sendEnhancedEcommerce(Map<String, Object> dataLayer) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(dataLayer);
    }

    protected void sendGeneralEvent(Map<String, Object> eventData) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(eventData);
    }
}
