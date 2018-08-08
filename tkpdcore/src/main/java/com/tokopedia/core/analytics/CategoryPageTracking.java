package com.tokopedia.core.analytics;

import java.util.Map;

/**
 * Created by nakama on 3/26/18.
 */

public class CategoryPageTracking extends TrackingUtils {

    public static void eventEnhance(Map<String, Object> dataLayer) {
        getGTMEngine().clearEnhanceEcommerce();
        getGTMEngine().eventTrackingEnhancedEcommerce(dataLayer);
    }

}
