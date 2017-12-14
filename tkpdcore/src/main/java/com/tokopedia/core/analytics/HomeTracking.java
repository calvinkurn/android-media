package com.tokopedia.core.analytics;

import java.util.List;

/**
 * Created by henrypriyono on 12/14/17.
 */

public class HomeTracking extends TrackingUtils {
    public static void enhanceClickFeedRecomItem(List<Object> objects,
                                           String eventCategory,
                                           String eventAction,
                                           String eventLabel,
                                           String productUrl,
                                           String actionField) {
        getGTMEngine().enhanceClickFeedRecomItem(objects, eventCategory, eventAction, eventLabel, productUrl, actionField);
    }
}
