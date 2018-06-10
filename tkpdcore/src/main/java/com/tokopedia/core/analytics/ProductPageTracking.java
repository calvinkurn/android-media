package com.tokopedia.core.analytics;

import java.util.Map;

/**
 * Created by nakama on 4/2/18.
 */

public class ProductPageTracking extends TrackingUtils {

    public static void eventEnhanceProductDetail(Map<String, Object> maps) {
        getGTMEngine().clearEnhanceEcommerce();
        getGTMEngine().eventTrackingEnhancedEcommerce(maps);
    }
}
