package com.tokopedia.purchase_platform.common.analytics;

import android.app.Activity;

import com.tokopedia.iris.util.IrisSession;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;

import java.util.HashMap;
import java.util.Map;
import com.tokopedia.iris.util.ConstantKt;

/**
 * @author anggaprasetiyo on 18/05/18.
 */
public abstract class TransactionAnalytics {

    protected TransactionAnalytics() {
    }

    Analytics getTracker() {
        return TrackApp.getInstance().getGTM();
    }

    public Map<String, Object> getGTMDataFromTrackAppUtils(String title) {

        return TrackAppUtils.gtmData(
                "",
                ConstantTransactionAnalytics.EventCategory.FIN_INSURANCE_CHECKOUT,
                ConstantTransactionAnalytics.EventAction.FIN_INSURANCE_CHECKOUT,
                String.format("checkout - %s", title)
        );
    }

    public void sendScreenName(Activity activity, String screenName) {
        Map<String, String> customDimension = new HashMap<>();
        customDimension.put(ConstantKt.KEY_SESSION_IRIS, new IrisSession(activity).getSessionId());

        TrackApp.getInstance().getGTM().sendScreenAuthenticated(screenName, customDimension);
    }

    public void sendScreenName(Activity activity, String screenName,  Map<String, String> customDimension) {
        customDimension.put(ConstantKt.KEY_SESSION_IRIS, new IrisSession(activity).getSessionId());

        TrackApp.getInstance().getGTM().sendScreenAuthenticated(screenName, customDimension);
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

    protected Map<String, Object> getGtmData(String event, String eventCategory,
                                             String eventAction, String eventLabel) {
        return TrackAppUtils.gtmData(event, eventCategory, eventAction, eventLabel);
    }
}
