package com.tokopedia.core.analytics;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;

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

    public static void eventClickGuidedSearch(String previousKey, String page, String nextKey) {
        sendGTMEvent(new EventTracking(
                "clickSearchResult",
                "search result",
                "click - guided search",
                String.format("%s - %s - %s", previousKey, page, nextKey)
        ).getEvent());
    }

    public static void eventImpressionGuidedSearch(String currentKey, String page) {
        sendGTMEvent(new EventTracking(
                "viewSearchResult",
                "search result",
                "impression - guided search",
                String.format("%s - %s", currentKey, page)
        ).getEvent());
    }
}
