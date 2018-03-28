package com.tokopedia.core.analytics;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;

import java.util.List;
import java.util.Map;

/**
 * Created by henrypriyono on 1/5/18.
 */

public class SearchTracking extends TrackingUtils {

    private static final String ACTION_FIELD = "/searchproduct - p$1 - product";

    public static String getActionFieldString(int pageNumber) {
        return ACTION_FIELD.replace("$1", Integer.toString(pageNumber));
    }

    public static void trackEventClickSearchResultProduct(Object item,
                                                          int pageNumber,
                                                          String eventLabel) {
        getGTMEngine().enhanceClickSearchResultProduct(item,
                eventLabel, getActionFieldString(pageNumber));
    }

    public static void eventImpressionSearchResultProduct(List<Object> list, String eventLabel) {
        getGTMEngine().enhanceImpressionSearchResultProduct(list, eventLabel);
    }

    public static void eventClickGuidedSearch(String previousKey, String page, String nextKey) {
        sendGTMEvent(new EventTracking(
                "clickSearchResult",
                "search result",
                "click - guided search",
                String.format("%s - %s - %s", previousKey, nextKey, page)
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
