package com.tokopedia.search.analytics;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.android.gms.tagmanager.DataLayer;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.tokopedia.discovery.common.model.WishlistTrackingModel;
import com.tokopedia.search.result.presentation.model.ProductItemViewModel;
import com.tokopedia.search.result.presentation.view.fragment.ProductListFragment;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.trackingoptimizer.TrackingQueue;
import com.tokopedia.user.session.UserSessionInterface;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.tokopedia.search.analytics.SearchTrackingConstant.EVENT;
import static com.tokopedia.search.analytics.SearchTrackingConstant.EVENT_ACTION;
import static com.tokopedia.search.analytics.SearchTrackingConstant.EVENT_CATEGORY;
import static com.tokopedia.search.analytics.SearchTrackingConstant.EVENT_LABEL;
import static com.tokopedia.search.analytics.SearchTrackingConstant.USER_ID;

/**
 * Created by henrypriyono on 1/5/18.
 */

public class SearchTracking {

    private static final String ACTION_FIELD = "/searchproduct - p$1 - product";
    public static final String ECOMMERCE = "ecommerce";
    public static final String EVENT_CATEGORY_SEARCH_RESULT_PROFILE = "search result profile";
    public static final String EVENT_CATEGORY_EMPTY_SEARCH = "empty search";
    public static final String EVENT_CATEGORY_SEARCH_RESULT = "search result";
    public static final String EVENT_ACTION_CLICK_PROFILE_RESULT = "click - profile result";
    public static final String PROMO_CLICK = "promoClick";
    public static final String PROMOTIONS = "promotions";
    public static final String VALUE_FOLLOW = "follow";
    public static final String VALUE_UNFOLLOW = "unfollow";
    public static final String EVENT_CLICK_SEARCH_RESULT = "clickSearchResult";
    public static final String EVENT_ACTION_CLICK_FOLLOW_ACTION_PROFILE = "click - %s profile";
    public static final String EVENT_ACTION_CLICK_NEW_SEARCH = "click - lakukan pencarian baru";
    public static final String EVENT_LABEL_CLICK_FOLLOW_ACTION_PROFILE = "keyword: %s - profile: %s - profile id: %s - po: %s";
    public static final String PROMO_VIEW = "promoView";
    public static final String EVENT_ACTION_IMPRESSION_PROFILE = "impression - profile";
    public static final String EVENT_ACTION_CLICK_SEE_ALL_NAV_WIDGET = "click - lihat semua widget";
    public static final String EVENT_ACTION_CLICK_WIDGET_DIGITAL_PRODUCT = "click widget - digital product";
    public static final String EVENT_ACTION_IMPRESSION_WIDGET_DIGITAL_PRODUCT = "impression widget - digital product";

    private UserSessionInterface userSessionInterface;

    @Inject
    public SearchTracking(Context context, UserSessionInterface userSessionInterface) {
        this.userSessionInterface = userSessionInterface;
    }

    private Map<String, Object> generateEventTrackingWithUserId(String event, String category, String action, String label) {
        Map<String, Object> eventTracking = new HashMap<>();

        eventTracking.put(EVENT, event);
        eventTracking.put(EVENT_CATEGORY, category);
        eventTracking.put(EVENT_ACTION, action);
        eventTracking.put(EVENT_LABEL, label);
        eventTracking.put(USER_ID, userSessionInterface.isLoggedIn() ? userSessionInterface.getUserId() : "0");

        return eventTracking;
    }

    public void sendGeneralEventWithUserId(String event, String category, String action, String label) {
        Map<String, Object> eventTrackingMap = generateEventTrackingWithUserId(event, category, action, label);

        sendGeneralEvent(eventTrackingMap);
    }

    public void sendGeneralEvent(Map<String, Object> eventTrackingMap) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(eventTrackingMap);
    }

    public static void screenTrackSearchSectionFragment(String screen) {
        if (TextUtils.isEmpty(screen)) {
            return;
        }

        TrackApp.getInstance().getGTM().sendScreenAuthenticated(screen);
    }

    public void eventSearchResultSort(String screenName, String sortByValue) {
        sendGeneralEventWithUserId(
                SearchEventTracking.Event.SEARCH_RESULT,
                SearchEventTracking.Category.SORT_BY,
                SearchEventTracking.Action.SORT_BY + " - " + screenName,
                sortByValue
        );
    }

    public void eventAppsFlyerViewListingSearch(Context context, JSONArray productsId, String keyword, ArrayList<String> prodIds) {
        Map<String, Object> listViewEvent = new HashMap<>();
        listViewEvent.put("af_content_id", prodIds);
        listViewEvent.put("af_currency", "IDR");
        listViewEvent.put("af_content_type", "product");
        listViewEvent.put("af_search_string", keyword);
        if (productsId.length() > 0) {
            listViewEvent.put("af_success", "success");
        } else {
            listViewEvent.put("af_success", "fail");
        }

        TrackApp.getInstance().getAppsFlyer().sendTrackEvent("af_search", listViewEvent);
    }

    public static void trackImpressionSearchResultShop(List<Object> shopItemList, String keyword) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, "promoView",
                        EVENT_CATEGORY, "search result",
                        EVENT_ACTION, "impression - shop",
                        EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(
                                "promoView", DataLayer.mapOf(
                                        "promotions", DataLayer.listOf(shopItemList.toArray(new Object[shopItemList.size()]))
                                )
                        )
                )
        );
    }

    public static void eventSearchResultShopItemClick(Object shopItem, String keyword) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, "promoClick",
                        EVENT_CATEGORY, "search result",
                        EVENT_ACTION, "click - shop",
                        EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(
                                "promoClick", DataLayer.mapOf(
                                        "promotions", shopItem
                                )
                        )
                )
        );
    }

    public static void eventImpressionSearchResultShopProductPreview(List<Object> shopItemProductList, String keyword) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, "productView",
                        EVENT_CATEGORY, "search result",
                        EVENT_ACTION, "impression - product - shop tab",
                        EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "impressions", DataLayer.listOf(
                                        shopItemProductList.toArray(new Object[shopItemProductList.size()])
                                ))
                )
        );
    }

    public static void eventSearchResultShopProductPreviewClick(Object shopItemProduct, String keyword) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, "productClick",
                        EVENT_CATEGORY, "search result",
                        EVENT_ACTION, "click - product - shop tab",
                        EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(
                                "click", DataLayer.mapOf(
                                        "actionField", DataLayer.mapOf("list", "/searchproduct - shop productlist"),
                                        "products", DataLayer.listOf(shopItemProduct)
                                )
                        )
                )
        );
    }

    public static void eventSearchResultShopItemClosedClick(Object shopItemClosed, String keyword) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, "promoClick",
                        EVENT_CATEGORY, "search result",
                        EVENT_ACTION, "click - shop - inactive",
                        EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(
                                "promoClick", DataLayer.mapOf(
                                        "promotions", shopItemClosed
                                )
                        )
                )
        );
    }

    public static String getActionFieldString(int pageNumber) {
        return ACTION_FIELD.replace("$1", Integer.toString(pageNumber));
    }

    public static void trackEventClickSearchResultProduct(ProductItemViewModel productItemViewModel,
                                                          Object item,
                                                          int pageNumber,
                                                          String eventLabel,
                                                          String filterSortParams) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf("event", "productClick",
                        "eventCategory", "search result",
                        "eventAction", "click - product",
                        "eventLabel", eventLabel,
                        "ecommerce", DataLayer.mapOf("click",
                                DataLayer.mapOf("actionField",
                                        DataLayer.mapOf("list", getActionFieldString(pageNumber)),
                                        "products", DataLayer.listOf(item)
                                )
                        ),
                        "searchFilter", filterSortParams
                )
        );
    }

    public static void eventImpressionSearchResultProduct(TrackingQueue trackingQueue,
                                                          List<Object> list,
                                                          List<ProductItemViewModel> productItemViewModels,
                                                          String eventLabel) {
        trackingQueue.putEETracking(
                (HashMap<String, Object>) DataLayer.mapOf("event", "productView",
                        "eventCategory", "search result",
                        "eventAction", "impression - product",
                        "eventLabel", eventLabel,
                        "ecommerce", DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "impressions", DataLayer.listOf(
                                        list.toArray(new Object[list.size()])
                                ))
                )
        );
    }

    private static int getPageNumberFromFirstItem(List<ProductItemViewModel> itemList) {
        if (itemList.get(0) != null) {
            return itemList.get(0).getPageNumber();
        }
        return 0;
    }

    private static double safeParseDouble(String price) {
        try {
            return Double.parseDouble(price);
        } catch (Exception e) {
            return 0;
        }
    }

    public static void eventClickGuidedSearch(String previousKey, int position, String nextKey) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                "clickSearchResult",
                "search result",
                "click - guided search",
                String.format("%s - %s - %s", previousKey, nextKey, String.valueOf(position + 1))
        ));
    }

    public static void eventClickRelatedSearch(Context context, String currentKeyword, String relatedKeyword) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                "clickSearchResult",
                "search result",
                "click - related keyword",
                String.format("%s - %s", currentKeyword, relatedKeyword)
        ));
    }

    public static void eventSearchResultChangeGrid(Context context, String gridName, String screenName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                SearchEventTracking.Event.SEARCH_RESULT,
                SearchEventTracking.Category.GRID_MENU,
                SearchEventTracking.Action.CLICK_CHANGE_GRID + gridName,
                screenName
        ));
    }

    public static void eventSearchResultCatalogClick(Context context, String keyword, String catalogName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                SearchEventTracking.Event.SEARCH_RESULT,
                SearchEventTracking.Category.SEARCH_RESULT.toLowerCase(),
                SearchEventTracking.Action.CLICK_CATALOG,
                keyword + " - " + catalogName
        ));
    }

    public static void eventSearchResultTabClick(Context context, String tabTitle) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                SearchEventTracking.Event.SEARCH_RESULT,
                SearchEventTracking.Category.SEARCH_TAB,
                SearchEventTracking.Action.CLICK_TAB,
                tabTitle
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

    public static String generateFilterAndSortEventLabel(Map<String, String> selectedFilter,
                                                         Map<String, String> selectedSort) {

        String filterEventLabel = generateFilterEventLabel(selectedFilter);
        String sortEventLabel = generateSortEventLabel(selectedSort);

        if (TextUtils.isEmpty(filterEventLabel)) {
            return sortEventLabel;
        } else {
            return filterEventLabel + "&" + sortEventLabel;
        }
    }

    public static void eventSearchNoResult(Context context,
                                           String keyword, String screenName,
                                           Map<String, String> selectedFilter) {

        eventSearchNoResult(keyword, screenName, selectedFilter, "", "");
    }

    public static void eventSearchNoResult(String keyword, String screenName,
                                           Map<String, String> selectedFilter,
                                           String alternativeKeyword,
                                           String resultCode) {

        TrackApp.getInstance().getGTM().sendGeneralEvent(
                SearchEventTracking.Event.EVENT_VIEW_SEARCH_RESULT,
                SearchEventTracking.Category.EVENT_TOP_NAV,
                String.format(SearchEventTracking.Action.NO_SEARCH_RESULT_WITH_TAB, screenName),
                String.format("keyword: %s - type: %s - alternative: %s - param: %s",
                        keyword,
                        !TextUtils.isEmpty(resultCode) ? resultCode : "none/other",
                        !TextUtils.isEmpty(alternativeKeyword) ? alternativeKeyword : "none/other",
                        generateFilterEventLabel(selectedFilter))
        );
    }

    public static void eventUserClickProfileResultInTabProfile(Object profileData,
                                                               String keyword) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        EVENT, PROMO_CLICK,
                        EVENT_CATEGORY, EVENT_CATEGORY_SEARCH_RESULT_PROFILE,
                        EVENT_ACTION, EVENT_ACTION_CLICK_PROFILE_RESULT,
                        EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(
                                PROMO_CLICK, DataLayer.mapOf(
                                        PROMOTIONS, DataLayer.listOf(
                                                profileData
                                        )
                                )
                        )
                )
        );
    }

    public static void eventClickFollowActionProfileResultProfileTab(Context context,
                                                                     String keyword,
                                                                     boolean isFollow,
                                                                     String profileName,
                                                                     String profileId,
                                                                     int position) {

        String foKey = "";
        if (isFollow) {
            foKey = VALUE_FOLLOW;
        } else {
            foKey = VALUE_UNFOLLOW;
        }

        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_SEARCH_RESULT,
                EVENT_CATEGORY_SEARCH_RESULT_PROFILE,
                String.format(
                        EVENT_ACTION_CLICK_FOLLOW_ACTION_PROFILE,
                        foKey
                ),
                String.format(
                        EVENT_LABEL_CLICK_FOLLOW_ACTION_PROFILE,
                        keyword,
                        profileName.toLowerCase(),
                        profileId,
                        position
                )
        ));
    }

    public static void eventUserImpressionProfileResultInTabProfile(TrackingQueue trackingQueue,
                                                               List<Object> profileListData,
                                                               String keyword) {
        trackingQueue.putEETracking(
                (HashMap<String, Object>) DataLayer.mapOf(
                        EVENT, PROMO_VIEW,
                        EVENT_CATEGORY, EVENT_CATEGORY_SEARCH_RESULT_PROFILE,
                        EVENT_ACTION, EVENT_ACTION_IMPRESSION_PROFILE,
                        EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(
                                PROMO_VIEW, DataLayer.mapOf(
                                        PROMOTIONS, profileListData
                                )
                        )
                )
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

    public static void eventUserClickSeeAllGlobalNavWidget(String keyword,
                                                           String productName,
                                                           String applink) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_SEARCH_RESULT,
                EVENT_CATEGORY_SEARCH_RESULT,
                EVENT_ACTION_CLICK_SEE_ALL_NAV_WIDGET,
                generateEventLabelGlobalNav(keyword, productName, applink)
        );
    }

    public static void trackEventClickGlobalNavWidgetItem(Object item,
                                                          String keyword,
                                                          String productName,
                                                          String applink) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, PROMO_CLICK,
                        EVENT_CATEGORY, EVENT_CATEGORY_SEARCH_RESULT,
                        EVENT_ACTION, EVENT_ACTION_CLICK_WIDGET_DIGITAL_PRODUCT,
                        EVENT_LABEL, generateEventLabelGlobalNav(keyword, productName, applink),
                        ECOMMERCE, DataLayer.mapOf(
                                PROMO_CLICK, DataLayer.mapOf(
                                        PROMOTIONS, DataLayer.listOf(item)
                                )
                        )
                )
        );
    }

    private static String generateEventLabelGlobalNav(String keyword, String productName, String applink) {
        return String.format("keyword: %s - product: %s - applink: %s", keyword, productName, applink);
    }

    public static void trackEventImpressionGlobalNavWidgetItem(TrackingQueue trackingQueue,
                                                               List<Object> list,
                                                               String keyword) {

        trackingQueue.putEETracking(
                (HashMap<String, Object>) DataLayer.mapOf(EVENT, PROMO_VIEW,
                        EVENT_CATEGORY, EVENT_CATEGORY_SEARCH_RESULT,
                        EVENT_ACTION, EVENT_ACTION_IMPRESSION_WIDGET_DIGITAL_PRODUCT,
                        EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(
                                PROMO_VIEW, DataLayer.mapOf(
                                        PROMOTIONS, DataLayer.listOf(
                                                list.toArray(new Object[list.size()])
                                        )
                                )
                        )
                )
        );
    }

    public static void eventSuccessWishlistSearchResultProduct(WishlistTrackingModel wishlistTrackingModel) {
        Map<String, Object> eventTrackingMap = new HashMap<>();

        eventTrackingMap.put(EVENT, SearchEventTracking.Event.CLICK_WISHLIST);
        eventTrackingMap.put(EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT.toLowerCase());
        eventTrackingMap.put(EVENT_ACTION, generateWishlistClickEventAction(wishlistTrackingModel.isAddWishlist(), wishlistTrackingModel.isUserLoggedIn()));
        eventTrackingMap.put(EVENT_LABEL, generateWishlistClickEventLabel(wishlistTrackingModel.getProductId(), wishlistTrackingModel.isTopAds(), wishlistTrackingModel.getKeyword()));

        TrackApp.getInstance().getGTM().sendGeneralEvent(eventTrackingMap);
    }

    private static String generateWishlistClickEventAction(boolean isAddWishlist, boolean isLoggedIn) {
        return getAddOrRemoveWishlistAction(isAddWishlist)
                + " - "
                + SearchEventTracking.Action.MODULE
                + " - "
                + getIsLoggedInWishlistAction(isLoggedIn);
    }

    private static String getAddOrRemoveWishlistAction(boolean isAddWishlist) {
        return isAddWishlist ? SearchEventTracking.Action.ADD_WISHLIST : SearchEventTracking.Action.REMOVE_WISHLIST;
    }

    private static String getIsLoggedInWishlistAction(boolean isLoggedIn) {
        return isLoggedIn ? SearchEventTracking.Action.LOGIN : SearchEventTracking.Action.NON_LOGIN;
    }

    private static String generateWishlistClickEventLabel(String productId, boolean isTopAds, String keyword) {
        return productId
                + " - "
                + getTopAdsOrGeneralLabel(isTopAds)
                + " - "
                + keyword;
    }

    private static String getTopAdsOrGeneralLabel(boolean isTopAds) {
        return isTopAds ? SearchEventTracking.Label.TOPADS : SearchEventTracking.Label.GENERAL;
    }

    public void eventActionClickCartButton(String keyword) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                SearchEventTracking.Event.CLICK_TOP_NAV,
                SearchEventTracking.Category.TOP_NAV_SEARCH_RESULT_PAGE,
                SearchEventTracking.Action.CLICK_CART_BUTTON_SEARCH_RESULT,
                keyword
        );
    }

    public void eventActionClickHomeButton(String keyword) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                SearchEventTracking.Event.CLICK_TOP_NAV,
                SearchEventTracking.Category.TOP_NAV_SEARCH_RESULT_PAGE,
                SearchEventTracking.Action.CLICK_HOME_BUTTON_SEARCH_RESULT,
                keyword
        );
    }

    public static void trackEventProductLongPress(String keyword, String productId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                SearchEventTracking.Event.SEARCH_RESULT,
                SearchEventTracking.Category.SEARCH_RESULT,
                SearchEventTracking.Action.LONG_PRESS_PRODUCT,
                String.format(SearchEventTracking.Label.KEYWORD_PRODUCT_ID, keyword, productId));
    }

    public static void trackEventClickSearchBar() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                SearchEventTracking.Event.CLICK_TOP_NAV,
                SearchEventTracking.Category.EVENT_TOP_NAV_SEARCH_SRP,
                SearchEventTracking.Action.CLICK_SEARCH_BOX,
                "");
    }

    public static void trackEventImpressionBannedProducts(String keyword) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                SearchEventTracking.Event.VIEW_SEARCH_RESULT_IRIS,
                SearchEventTracking.Category.SEARCH_RESULT,
                SearchEventTracking.Action.IMPRESSION_BANNED_PRODUCT_TICKER_RELATED,
                keyword
        );
    }

    public static void trackEventClickGoToBrowserBannedProducts(String keyword) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                SearchEventTracking.Event.VIEW_SEARCH_RESULT_IRIS,
                SearchEventTracking.Category.SEARCH_RESULT,
                SearchEventTracking.Action.CLICK_BANNED_PRODUCT_TICKER_RELATED,
                keyword
        );
    }
}
