package com.tokopedia.core.analytics;

import com.tokopedia.core.analytics.data.purchase.PurchaseTrackingData;

import java.util.List;
import java.util.Map;

/**
 * Created by okasurya on 12/8/17.
 */

public class PurchaseTracking extends TrackingUtils {
    public static void marketplace(String name, Map<String, Object> data) {
        getGTMEngine().event(name, data);
    }

    public static void digital(String name, Map<String, Object> data) {
        getGTMEngine().event(name, data);
    }
}
