package com.tokopedia.transactionanalytics;

import android.app.Activity;
import android.util.Log;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;

import java.util.Map;

/**
 * @author anggaprasetiyo on 18/05/18.
 */
public abstract class TransactionAnalytics {

    TransactionAnalytics() {
    }

    Analytics getTracker(){
        return  TrackApp.getInstance().getGTM();
    }

    public void sendScreenName(Activity activity, String screenName) {
        TrackApp.getInstance().getGTM().sendScreenAuthenticated(screenName);
    }

    void sendEventCategoryActionLabel(String event, String eventCategory,
                                      String eventAction, String eventLabel) {

        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                event, eventCategory, eventAction, eventLabel));
    }


    void sendEventCategoryAction(String event, String eventCategory,
                                 String eventAction) {
        sendEventCategoryActionLabel(event, eventCategory, eventAction, "");
    }


    void sendEnhancedEcommerce(Map<String, Object> dataLayer) {
        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(dataLayer);
    }
}
