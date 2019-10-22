package com.tokopedia.imagesearch.analytics;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tokopedia.abstraction.common.utils.view.DateFormatUtils.DEFAULT_LOCALE;
import static com.tokopedia.imagesearch.analytics.ImageSearchTrackingConstant.USER_ID;

/**
 * Created by henrypriyono on 1/5/18.
 */

public class ImageSearchTracking {

    public static final String ACTION_IMAGE_SEARCH = "/imagesearch";
    public static final String EVENT = "event";
    public static final String EVENT_CATEGORY = "eventCategory";
    public static final String EVENT_ACTION = "eventAction";
    public static final String EVENT_LABEL = "eventLabel";
    public static final String ECOMMERCE = "ecommerce";
    public static final String EVENT_CATEGORY_EMPTY_SEARCH = "empty search";
    public static final String EVENT_CLICK_SEARCH_RESULT = "clickSearchResult";
    public static final String EVENT_ACTION_CLICK_NEW_SEARCH = "click - lakukan pencarian baru";

    public void eventSearchImagePickerClickCamera() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                ImageSearchEventTracking.Event.IMAGE_SEARCH_CLICK,
                ImageSearchEventTracking.Category.IMAGE_SEARCH,
                ImageSearchEventTracking.Action.SEARCH_IMAGE_PICKER_CLICK_CAMERA,
                "");
    }

    public void eventSearchImagePickerClickGallery() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                ImageSearchEventTracking.Event.IMAGE_SEARCH_CLICK,
                ImageSearchEventTracking.Category.IMAGE_SEARCH,
                ImageSearchEventTracking.Action.SEARCH_IMAGE_PICKER_CLICK_GALLERY,
                "");
    }

    public static void trackEventClickImageSearchResultProduct(Object item) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                EVENT, ImageSearchEventTracking.Event.PRODUCT_CLICK,
                EVENT_CATEGORY, ImageSearchEventTracking.Category.IMAGE_SEARCH_RESULT,
                EVENT_ACTION, ImageSearchEventTracking.Action.CLICK_PRODUCT,
                EVENT_LABEL, "",
                ECOMMERCE, DataLayer.mapOf(
                        "click", DataLayer.mapOf(
                                    "actionField", DataLayer.mapOf("list", ACTION_IMAGE_SEARCH),
                                             "products", DataLayer.listOf(item)
                                        )
                            )
                )
        );
    }

    public static void eventImpressionImageSearchResultProduct(Context context, List<Object> list) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf("event", "productView",
                        "eventCategory", ImageSearchEventTracking.Category.IMAGE_SEARCH_RESULT,
                        "eventAction", "impression - product",
                        "eventLabel", "",
                        "ecommerce", DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "impressions", DataLayer.listOf(
                                        list.toArray(new Object[list.size()])
                                ))
                )
        );
    }

    public static void eventSearchResultShare(Context context, String screenName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                ImageSearchEventTracking.Event.SEARCH_RESULT,
                ImageSearchEventTracking.Category.SEARCH_SHARE,
                ImageSearchEventTracking.Action.CLICK_BAR + screenName,
                ""
        ));
    }

    public static void eventSearchResultChangeGrid(Context context, String gridName, String screenName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                ImageSearchEventTracking.Event.SEARCH_RESULT,
                ImageSearchEventTracking.Category.GRID_MENU,
                ImageSearchEventTracking.Action.CLICK_CHANGE_GRID + gridName,
                screenName
        ));
    }

    public static void eventSearchResultCatalogClick(Context context, String keyword, String catalogName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                ImageSearchEventTracking.Event.SEARCH_RESULT,
                ImageSearchEventTracking.Category.SEARCH_RESULT.toLowerCase(),
                ImageSearchEventTracking.Action.CLICK_CATALOG,
                keyword + " - " + catalogName
        ));
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

    public static void eventSearchNoResult(Context context,
                                           String keyword, String screenName,
                                           Map<String, String> selectedFilter) {

        eventSearchNoResult(keyword, screenName, selectedFilter, "", "");
    }

    private static void eventSearchNoResult(String keyword, String screenName,
                                           Map<String, String> selectedFilter,
                                           String alternativeKeyword,
                                           String resultCode) {

        TrackApp.getInstance().getGTM().sendGeneralEvent(
                ImageSearchEventTracking.Event.EVENT_VIEW_SEARCH_RESULT,
                ImageSearchEventTracking.Category.EVENT_TOP_NAV,
                String.format(ImageSearchEventTracking.Action.NO_SEARCH_RESULT_WITH_TAB, screenName),
                String.format("keyword: %s - type: %s - alternative: %s - param: %s",
                        keyword,
                        !TextUtils.isEmpty(resultCode) ? resultCode : "none/other",
                        !TextUtils.isEmpty(alternativeKeyword) ? alternativeKeyword : "none/other",
                        generateFilterEventLabel(selectedFilter))
        );
    }

    public static void eventUserClickNewSearchOnEmptySearch(Context context, String screenName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_SEARCH_RESULT,
                EVENT_CATEGORY_EMPTY_SEARCH,
                EVENT_ACTION_CLICK_NEW_SEARCH,
                String.format("tab: %s", screenName)
        );
    }

    public static void eventSearchResultProductWishlistClick(boolean isWishlisted, String keyword, String userId) {
        sendGeneralEventWithUserId(
                ImageSearchEventTracking.Event.PRODUCT_VIEW,
                ImageSearchEventTracking.Category.SEARCH_RESULT.toLowerCase(),
                ImageSearchEventTracking.Action.CLICK_WISHLIST,
                generateWishlistClickEventLabel(isWishlisted, keyword),
                userId
        );
    }

    private static void sendGeneralEventWithUserId(String event, String category, String action, String label, String userId) {
        Map<String, Object> eventTrackingMap = generateEventTrackingWithUserId(event, category, action, label, userId);

        sendGeneralEvent(eventTrackingMap);
    }

    private static Map<String, Object> generateEventTrackingWithUserId(String event, String category, String action, String label, String userId) {
        Map<String, Object> eventTracking = new HashMap<>();

        eventTracking.put(EVENT, event);
        eventTracking.put(EVENT_CATEGORY, category);
        eventTracking.put(EVENT_ACTION, action);
        eventTracking.put(EVENT_LABEL, label);
        eventTracking.put(USER_ID, userId);

        return eventTracking;
    }

    private static void sendGeneralEvent(Map<String, Object> eventTrackingMap) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(eventTrackingMap);
    }

    private static String generateWishlistClickEventLabel(boolean isWishlisted, String keyword) {
        String action = isWishlisted ? "add" : "remove";
        return action + " - " + keyword + " - " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", DEFAULT_LOCALE).format(new Date());
    }

    public static void trackEventProductLongPress(String keyword, String productId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                "clickSearchResult",
                "search result",
                "click - long press product",
                String.format("Keyword: %s - product id: %s", keyword, productId));
    }
}
