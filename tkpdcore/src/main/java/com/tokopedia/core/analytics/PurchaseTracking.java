package com.tokopedia.core.analytics;

import java.util.Map;

/**
 * Created by okasurya on 12/8/17.
 */

public class PurchaseTracking extends TrackingUtils {
    public static final String TRANSACTION = "transaction";

    public static void marketplace(Map<String, Object> data) {
        getGTMEngine().eventTrackingEnhancedEcommerce(data);
        getGTMEngine().sendScreen(AppScreen.SCREEN_FINISH_TX);
        getGTMEngine().clearEnhanceEcommerce();
    }

    public static void digital(Map<String, Object> data) {
        getGTMEngine().event(TRANSACTION, data);
        getGTMEngine().sendScreen(AppScreen.SCREEN_FINISH_TX);
        getGTMEngine().clearEnhanceEcommerce();
    }
}
