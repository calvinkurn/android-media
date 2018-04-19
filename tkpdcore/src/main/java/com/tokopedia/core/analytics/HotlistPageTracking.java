package com.tokopedia.core.analytics;

import java.util.Map;

/**
 * Created by nakama on 3/23/18.
 */

public class HotlistPageTracking extends TrackingUtils {

    public static void eventEnhance(Map<String, Object> dataLayer) {
        getGTMEngine().clearEnhanceEcommerce();
        getGTMEngine().eventTrackingEnhancedEcommerce(dataLayer);
    }
}
