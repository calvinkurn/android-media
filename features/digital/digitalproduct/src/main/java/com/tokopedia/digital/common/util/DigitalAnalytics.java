package com.tokopedia.digital.common.util;

import android.app.Activity;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;

import java.util.Map;

import static com.tokopedia.digital.common.constant.DigitalEventTracking.Action.CLICK_PANDUAN_SECTION;
import static com.tokopedia.digital.common.constant.DigitalEventTracking.Category.DIGITAL_NATIVE;
import static com.tokopedia.digital.common.constant.DigitalEventTracking.Event.DIGITAL_GENERAL_EVENT;

/**
 * @author by furqan on 13/08/18.
 */

public class DigitalAnalytics {

    private AnalyticTracker analyticTracker;

    public DigitalAnalytics() {
        this.analyticTracker = new AnalyticTracker() {
            @Override
            public void sendEventTracking(Map<String, Object> events) {
                UnifyTracking.eventClearEnhanceEcommerce();
                UnifyTracking.sendGTMEvent(events);
            }

            @Override
            public void sendEventTracking(String event, String category, String action, String label) {
                UnifyTracking.sendGTMEvent(new EventTracking(
                        event,
                        category,
                        action,
                        label
                ).getEvent());
            }

            @Override
            public void sendScreen(Activity activity, final String screenName) {
                if(activity != null && !TextUtils.isEmpty(screenName)) {
                    ScreenTracking.sendScreen(activity, () -> screenName);
                }
            }

            @Override
            public void sendEnhancedEcommerce(Map<String, Object> trackingData) {
                TrackingUtils.eventTrackingEnhancedEcommerce(trackingData);
            }
        };
    }

    public void eventClickPanduanPage(String categoryName) {
        analyticTracker.sendEventTracking(
                DIGITAL_GENERAL_EVENT,
                DIGITAL_NATIVE,
                CLICK_PANDUAN_SECTION,
                categoryName
        );
    }
}
