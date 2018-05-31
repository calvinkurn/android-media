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
                                                          Map<String, String> selectedFilter,
                                                          Map<String, String> selectedSort) {
        getGTMEngine().enhanceClickSearchResultProduct(item,
                eventLabel, getActionFieldString(pageNumber),
                concatFilterAndSortEventLabel(
                        generateFilterEventLabel(selectedFilter),
                        generateSortEventLabel(selectedSort)
                ));
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

    public static void eventSearchResultCloseBottomSheetFilter(String screenName, Map<String, String> selectedFilter) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.FILTER_PRODUCT,
                AppEventTracking.Action.APPLY_FILTER.toLowerCase() + " - " + screenName,
                generateFilterEventLabel(selectedFilter)
        ).setUserId().getEvent());
    }

    private static String generateFilterEventLabel(Map<String, String> selectedFilter) {
        if (selectedFilter == null) {
            return "";
        }
        List<String> filterList = new ArrayList<>();
        for (Map.Entry<String, String> entry : selectedFilter.entrySet()) {
            filterList.add(entry.getKey() + "=" + entry.getValue());
        }
        return TextUtils.join("&", filterList);
    }

    private static String generateSortEventLabel(Map<String, String> selectedSort) {
        if (selectedSort == null) {
            return "";
        }
        List<String> sortList = new ArrayList<>();
        for (Map.Entry<String, String> entry : selectedSort.entrySet()) {
            sortList.add(entry.getKey() + "=" + entry.getValue());
        }
        return TextUtils.join("&", sortList);
    }

    private static String concatFilterAndSortEventLabel(String filterEventLabel, String sortEventLabel) {
        if (TextUtils.isEmpty(filterEventLabel)) {
            return sortEventLabel;
        } else {
            return filterEventLabel + "&" + sortEventLabel;
        }
    }

    public static void eventSearchResultFilterJourney(String filterName,
                                                      String filterValue,
                                                      boolean isInsideDetail, boolean isActive) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.FILTER_JOURNEY,
                AppEventTracking.Action.CLICK.toLowerCase() + " - "
                        + filterName + ": " + filterValue + " - "
                        + (isInsideDetail ? "inside lihat semua" : "outside lihat semua"),
                Boolean.toString(isActive)
        ).setUserId().getEvent());
    }

    public static void eventSearchResultApplyFilterDetail(String filterName) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.FILTER_JOURNEY,
                "click simpan on lihat semua " + filterName,
                ""
        ).setUserId().getEvent());
    }

    public static void eventSearchResultBackFromFilterDetail(String filterName) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.FILTER_JOURNEY,
                "click back on lihat semua " + filterName,
                ""
        ).setUserId().getEvent());
    }

    public static void eventSearchResultNavigateToFilterDetail(String filterName) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.FILTER_JOURNEY,
                "click lihat semua " + filterName,
                ""
        ).setUserId().getEvent());
    }

    public static void eventSearchResultOpenFilterPageProduct() {
        eventSearchResultOpenFilterPage("product");
    }

    public static void eventSearchResultOpenFilterPageCatalog() {
        eventSearchResultOpenFilterPage("catalog");
    }

    public static void eventSearchResultOpenFilterPageShop() {
        eventSearchResultOpenFilterPage("shop");
    }

    private static void eventSearchResultOpenFilterPage(String tabName) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.FILTER.toLowerCase() + " " + tabName,
                AppEventTracking.Action.CLICK_FILTER,
                ""
        ).setUserId().getEvent());
    }

    public static void eventSearchNoResult(String keyword, String screenName) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.NO_RESULT,
                AppEventTracking.Category.EVENT_TOP_NAV,
                AppEventTracking.Action.NO_SEARCH_RESULT,
                "keyword: " + keyword + " - tab: " + screenName
        ).getEvent());
    }
}
