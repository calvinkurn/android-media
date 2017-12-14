package com.tokopedia.core.analytics;

import java.util.Map;

/**
 * Created by okasurya on 12/8/17.
 */

public class PurchaseTracking extends TrackingUtils {
    public static final String TRANSACTION = "transaction";

    public static void marketplace(String name, Map<String, Object> data) {
        getGTMEngine().event(name, data);
    }

    public static void digital(String name, Map<String, Object> data) {
        getGTMEngine().event(name, data);
    }
}
