package com.tokopedia.core.analytics;

import android.content.Context;

import java.util.Map;

/**
 * Created by nakama on 3/26/18.
 */

public class CategoryPageTracking extends TrackingUtils {

    public static void eventEnhance(Context context, Map<String, Object> dataLayer) {
        getGTMEngine(context).clearEnhanceEcommerce();
        getGTMEngine(context).eventTrackingEnhancedEcommerce(dataLayer);
    }

}
