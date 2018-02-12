package com.tokopedia.core.analytics;

import com.tkpd.library.utils.CurrencyFormatHelper;

import java.util.List;
import java.util.Map;

/**
 * Created by henrypriyono on 1/5/18.
 */

public class SearchTracking extends TrackingUtils {

    public static final String ACTION_FIELD = "/search result - product 2 - product list";

    public static void trackEventClickSearchResultProduct(Object item,
                                                          String eventLabel) {
        getGTMEngine().enhanceClickSearchResultProduct(item, eventLabel, ACTION_FIELD);
    }

    public static void eventImpressionSearchResultProduct(List<Object> list, String eventLabel) {
        getGTMEngine().enhanceImpressionSearchResultProduct(list, eventLabel);
    }
}
