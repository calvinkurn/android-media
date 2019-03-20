package com.tokopedia.core.analytics;

import android.content.Context;

import com.tokopedia.track.TrackApp;

import java.util.Map;

/**
 * Created by nakama on 3/26/18.
 */

public class CategoryPageTracking extends TrackingUtils {

    public static void eventEnhance(Context context, Map<String, Object> dataLayer) {
        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(dataLayer);
    }

}
