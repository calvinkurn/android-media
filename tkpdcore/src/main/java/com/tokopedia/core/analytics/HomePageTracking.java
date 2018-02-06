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
        sendGTMEvent(new EventTracking(
                DEFAULT_VALUE_EVENT_NAME,
                DEFAULT_VALUE_EVENT_CATEGORY,
                "slider banner click view all",
                ""
        ).getEvent());
    }


}
