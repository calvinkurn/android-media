package com.tokopedia.imagesearch.analytics;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
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

    public static void trackEventClickImageSearchResultProduct(Object item, String token) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                EVENT, ImageSearchEventTracking.Event.PRODUCT_CLICK,
                EVENT_CATEGORY, ImageSearchEventTracking.Category.IMAGE_SEARCH_RESULT,
                EVENT_ACTION, ImageSearchEventTracking.Action.CLICK_PRODUCT,
                EVENT_LABEL, token,
                ECOMMERCE, DataLayer.mapOf(
                        "click", DataLayer.mapOf(
                                    "actionField", DataLayer.mapOf("list", ACTION_IMAGE_SEARCH),
                                             "products", DataLayer.listOf(item)
                                        )
                            )
                )
        );
    }

    public static void eventImpressionImageSearchResultProduct(List<Object> list, String token) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf("event", "productView",
                        "eventCategory", ImageSearchEventTracking.Category.IMAGE_SEARCH_RESULT,
                        "eventAction", "impression - product",
                        "eventLabel", token,
                        "ecommerce", DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "impressions", DataLayer.listOf(
                                        list.toArray(new Object[list.size()])
                                ))
                )
        );
    }

    public static void eventSearchResultProductWishlistClick(boolean isWishlisted, String keyword, String userId, String token) {
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

    public static void trackEventProductLongPress(String keyword, String productId, String token) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                "clickSearchResult",
                "search result",
                "click - long press product",
                String.format("Keyword: %s - product id: %s", keyword, productId));
    }
}
