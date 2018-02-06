package com.tokopedia.core.analytics;

import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.analytics.nishikino.model.Promotion;

/**
 * Created by nakama on 2/6/18.
 */

public class HomePageTracking extends TrackingUtils {

    public static String DEFAULT_VALUE_EVENT_NAME = "clickHomepage";
    public static String DEFAULT_VALUE_EVENT_CATEGORY = "homepage";

    public static void eventPromoImpression(Promotion promotion) {
        getGTMEngine().clearEnhanceEcommerce();
        getGTMEngine().eventTrackingEnhancedEcommerce(promotion.getImpressionDataLayer());
    }

    public static void eventPromoClick(Promotion promotion) {
        getGTMEngine().clearEnhanceEcommerce();
        getGTMEngine().eventTrackingEnhancedEcommerce(promotion.getClickDataLayer());
    }

    public static void eventClickViewAllPromo() {
        flushEventTracker();
        sendGTMEvent(new EventTracking(
                DEFAULT_VALUE_EVENT_NAME,
                DEFAULT_VALUE_EVENT_CATEGORY,
                "slider banner click view all",
                ""
        ).getEvent());
    }

    private static void flushEventTracker() {
        sendGTMEvent(new EventTracking(
                null, null, null, null
        ).getEvent());
    }

    public static void eventClickHomeUseCase(String title) {
        flushEventTracker();
        sendGTMEvent(new EventTracking(
                DEFAULT_VALUE_EVENT_NAME,
                DEFAULT_VALUE_EVENT_CATEGORY,
                "click 5 use cases",
                title
        ).getEvent());
    }

    public static void eventClickDynamicIcons(String title) {
        flushEventTracker();
        sendGTMEvent(new EventTracking(
                DEFAULT_VALUE_EVENT_NAME,
                DEFAULT_VALUE_EVENT_CATEGORY,
                "click 5 dynamic icons",
                title
        ).getEvent());
    }


}
