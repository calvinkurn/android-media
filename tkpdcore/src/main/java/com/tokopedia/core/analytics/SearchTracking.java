package com.tokopedia.core.analytics;

import android.text.TextUtils;

import com.tokopedia.core.analytics.nishikino.model.EventTracking;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by henrypriyono on 1/5/18.
 */

public class SearchTracking extends TrackingUtils {

    private static final String ACTION_FIELD = "/searchproduct - p$1 - product";
    public static String imageClick = "/imagesearch - p%s";

    public static String getActionFieldString(int pageNumber) {
        return ACTION_FIELD.replace("$1", Integer.toString(pageNumber));
    }

    public static void trackEventClickSearchResultProduct(Object item,
                                                          int pageNumber,
                                                          String eventLabel,
                                                          Map<String, String> selectedFilter) {
        getGTMEngine().enhanceClickSearchResultProduct(item,
                eventLabel, getActionFieldString(pageNumber), generateFilterEventLabel(selectedFilter));
    }

    public static void trackEventClickImageSearchResultProduct(Object item, int position) {
        getGTMEngine().enhanceClickImageSearchResultProduct(item, String.format(imageClick, position));
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.PRODUCT_CLICK,
                AppEventTracking.Category.IMAGE_SEARCH_RESULT,
                AppEventTracking.Action.CLICK_PRODUCT,
                ""
        ).getEvent());
    }

    public static void eventImpressionSearchResultProduct(List<Object> list, String eventLabel) {
        getGTMEngine().enhanceImpressionSearchResultProduct(list, eventLabel);
    }

    public static void eventImpressionImageSearchResultProduct(List<Object> list) {
        getGTMEngine().enhanceImpressionImageSearchResultProduct(list);
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

    public static void eventSearchResultShopItemClick(String keyword, String shopName,
                                                      int page, int position) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.SEARCH_RESULT.toLowerCase(),
                AppEventTracking.Action.CLICK_SHOP,
                keyword + " - " + shopName + " - " + Integer.toString(page) + " - " + Integer.toString(position)
        ).setUserId().getEvent());
    }

    public static void eventSearchResultShare(String screenName) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.SEARCH_SHARE,
                AppEventTracking.Action.CLICK_BAR + screenName,
                ""
        ).setUserId().getEvent());
    }

    public static void eventImageSearchResultChangeGrid(String gridName) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.IMAGE_SEARCH_CLICK,
                AppEventTracking.Category.IMAGE_SEARCH,
                AppEventTracking.Action.CLICK_CHANGE_GRID,
                gridName
        ).setUserId().getEvent());
    }

    public static void eventSearchResultChangeGrid(String gridName, String screenName) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.GRID_MENU,
                AppEventTracking.Action.CLICK_CHANGE_GRID + gridName,
                screenName
        ).setUserId().getEvent());
    }

    public static void eventSearchResultFavoriteShopClick(String keyword, String shopName,
                                                          int page, int position) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.SEARCH_RESULT.toLowerCase(),
                AppEventTracking.Action.FAVORITE_SHOP_CLICK,
                keyword + " - " + shopName + " - " + Integer.toString(page) + " - " + Integer.toString(position)
        ).setUserId().getEvent());
    }

    public static void eventSearchResultCatalogClick(String keyword, String catalogName) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.SEARCH_RESULT.toLowerCase(),
                AppEventTracking.Action.CLICK_CATALOG,
                keyword + " - " + catalogName
        ).setUserId().getEvent());
    }

    public static void eventSearchResultTabClick(String tabTitle) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.SEARCH_TAB,
                AppEventTracking.Action.CLICK_TAB,
                tabTitle
        ).setUserId().getEvent());
    }

    public static void eventSearchResultFilter(String screenName, Map<String, String> selectedFilter) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.FILTER_PRODUCT,
                AppEventTracking.Action.FILTER.toLowerCase() + " - " + screenName,
                generateFilterEventLabel(selectedFilter)
        ).setUserId().getEvent());
    }

    private static String generateFilterEventLabel(Map<String, String> selectedFilter) {
        List<String> filterList = new ArrayList<>();
        for (Map.Entry<String, String> entry : selectedFilter.entrySet()) {
            filterList.add(entry.getKey() + "=" + entry.getValue());
        }
        return TextUtils.join("&", filterList);
    }
}
