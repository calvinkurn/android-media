package com.tokopedia.search.analytics;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.analytic_constant.Event;
import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.discovery.common.model.WishlistTrackingModel;
import com.tokopedia.iris.util.ConstantKt;
import com.tokopedia.search.result.presentation.model.ProductItemDataView;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.trackingoptimizer.TrackingQueue;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tokopedia.search.analytics.SearchEventTracking.BUSINESS_UNIT;
import static com.tokopedia.search.analytics.SearchEventTracking.CURRENT_SITE;
import static com.tokopedia.search.analytics.SearchEventTracking.SEARCH;
import static com.tokopedia.search.analytics.SearchEventTracking.TOKOPEDIA_MARKETPLACE;
import static com.tokopedia.search.analytics.SearchTrackingConstant.CATEGORY_ID_MAPPING;
import static com.tokopedia.search.analytics.SearchTrackingConstant.CATEGORY_NAME_MAPPING;
import static com.tokopedia.search.analytics.SearchTrackingConstant.EVENT;
import static com.tokopedia.search.analytics.SearchTrackingConstant.EVENT_ACTION;
import static com.tokopedia.search.analytics.SearchTrackingConstant.EVENT_CATEGORY;
import static com.tokopedia.search.analytics.SearchTrackingConstant.EVENT_LABEL;
import static com.tokopedia.search.analytics.SearchTrackingConstant.IS_RESULT_FOUND;
import static com.tokopedia.search.analytics.SearchTrackingConstant.RELATED_KEYWORD;
import static com.tokopedia.search.analytics.SearchTrackingConstant.USER_ID;

/**
 * Created by henrypriyono on 1/5/18.
 */

public class SearchTracking {

    private static final String ACTION_FIELD = "/searchproduct - %s";
    private static final String ORGANIC = "organic";
    private static final String ORGANIC_ADS = "organic ads";
    public static final String ECOMMERCE = "ecommerce";
    public static final String EVENT_CATEGORY_EMPTY_SEARCH = "empty search";
    public static final String EVENT_CATEGORY_SEARCH_RESULT = "search result";
    public static final String PROMO_CLICK = "promoClick";
    public static final String PROMOTIONS = "promotions";
    public static final String EVENT_CLICK_SEARCH_RESULT = "clickSearchResult";
    public static final String EVENT_ACTION_CLICK_NEW_SEARCH = "click - lakukan pencarian baru";
    public static final String PROMO_VIEW = "promoView";
    public static final String EVENT_ACTION_CLICK_SEE_ALL_NAV_WIDGET = "click - lihat semua widget";
    public static final String EVENT_ACTION_IMPRESSION_WIDGET_DIGITAL_PRODUCT = "impression widget - digital product";

    public static void screenTrackSearchSectionFragment(String screen) {
        if (TextUtils.isEmpty(screen)) {
            return;
        }

        TrackApp.getInstance().getGTM().sendScreenAuthenticated(screen);
    }

    public static void eventSearchResultSort(String screenName, String sortByValue, String userId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT,SearchEventTracking.Event.SEARCH_RESULT,
                        EVENT_CATEGORY, SearchEventTracking.Category.SORT_BY,
                        EVENT_ACTION, SearchEventTracking.Action.SORT_BY + " - " + screenName,
                        EVENT_LABEL, sortByValue,
                        USER_ID, userId
                )
        );
    }

    public static void eventAppsFlyerViewListingSearch(JSONArray productsId, String keyword, ArrayList<String> prodIds) {
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

    public static void trackEventClickQuickFilter(String filterName, String filterValue, boolean isSelected, String userId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT, SearchEventTracking.Event.SEARCH_RESULT,
                        EVENT_CATEGORY, SearchEventTracking.Category.FILTER_PRODUCT,
                        EVENT_ACTION, SearchEventTracking.Action.QUICK_FILTER,
                        EVENT_LABEL, filterName + " - " + filterValue + " - " + isSelected,
                        USER_ID, userId
                )
        );
    }

    public static void trackImpressionSearchResultShop(List<Object> shopItemList, String keyword) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, SearchEventTracking.Event.PROMO_VIEW,
                        EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        EVENT_ACTION, SearchEventTracking.Action.IMPRESSION_SHOP,
                        EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(
                                "promoView", DataLayer.mapOf(
                                        "promotions", DataLayer.listOf(shopItemList.toArray(new Object[shopItemList.size()]))
                                )
                        )
                )
        );
    }

    public static void eventSearchResultShopItemClick(Object shopItem, String shopId, String keyword) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, SearchEventTracking.Event.PROMO_CLICK,
                        EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        EVENT_ACTION, SearchEventTracking.Action.CLICK_SHOP,
                        EVENT_LABEL, shopId + " - " + keyword,
                        ECOMMERCE, DataLayer.mapOf(
                                "promoClick", DataLayer.mapOf(
                                        "promotions", DataLayer.listOf(shopItem)
                                )
                        )
                )
        );
    }

    public static void eventImpressionSearchResultShopProductPreview(List<Object> shopItemProductList, String keyword) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, SearchEventTracking.Event.PRODUCT_VIEW,
                        EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        EVENT_ACTION, SearchEventTracking.Action.IMPRESSION_PRODUCT_SHOP_TAB,
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
                DataLayer.mapOf(EVENT, SearchEventTracking.Event.PRODUCT_CLICK,
                        EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        EVENT_ACTION, SearchEventTracking.Action.CLICK_PRODUCT_SHOP_TAB,
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

    public static void eventSearchResultShopItemClosedClick(Object shopItemClosed, String shopId, String keyword) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, SearchEventTracking.Event.PROMO_CLICK,
                        EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        EVENT_ACTION, SearchEventTracking.Action.CLICK_SHOP_INACTIVE,
                        EVENT_LABEL, shopId + " - " + keyword,
                        ECOMMERCE, DataLayer.mapOf(
                                "promoClick", DataLayer.mapOf(
                                        "promotions", DataLayer.listOf(shopItemClosed)
                                )
                        )
                )
        );
    }

    public static void trackEventClickSearchResultProduct(Object item,
                                                          boolean isOrganicAds,
                                                          String eventLabel,
                                                          String filterSortParams) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf("event", "productClick",
                        "eventCategory", "search result",
                        "eventAction", "click - product",
                        "eventLabel", eventLabel,
                        "ecommerce", DataLayer.mapOf("click",
                                DataLayer.mapOf("actionField",
                                        DataLayer.mapOf("list", getActionFieldString(isOrganicAds)),
                                        "products", DataLayer.listOf(item)
                                )
                        ),
                        "searchFilter", filterSortParams
                )
        );
    }

    private static String getActionFieldString(boolean isOrganicAds) {
        String organicStatus = isOrganicAds ? ORGANIC_ADS : ORGANIC;

        return String.format(ACTION_FIELD, organicStatus);
    }

    public static void eventImpressionSearchResultProduct(TrackingQueue trackingQueue,
                                                          List<Object> list,
                                                          String eventLabel,
                                                          String irisSessionId) {
        HashMap<String, Object> map = (HashMap<String, Object>) DataLayer.mapOf("event", "productView",
                "eventCategory", "search result",
                "eventAction", "impression - product",
                "eventLabel", eventLabel,
                "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", DataLayer.listOf(
                                list.toArray(new Object[list.size()])
                        ))
        );
        if(!TextUtils.isEmpty(irisSessionId))
            map.put(ConstantKt.KEY_SESSION_IRIS, irisSessionId);

        trackingQueue.putEETracking(
                map
        );
    }

    private static int getPageNumberFromFirstItem(List<ProductItemDataView> itemList) {
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

    public static void eventClickSuggestedSearch(String currentKeyword, String suggestion) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                SearchEventTracking.Event.SEARCH_RESULT,
                SearchEventTracking.Category.SEARCH_RESULT,
                SearchEventTracking.Action.CLICK_FUZZY_KEYWORDS_SUGGESTION,
                String.format("%s - %s", currentKeyword, suggestion)
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

    public static void eventSearchNoResult(Context context,
                                           String keyword, String screenName,
                                           Map<String, String> selectedFilter) {

        eventSearchNoResult(keyword, screenName, selectedFilter, "", "", "");
    }

    public static void eventSearchNoResult(String keyword, String screenName,
                                           Map<String, String> selectedFilter,
                                           String alternativeKeyword,
                                           String resultCode,
                                           String keywordProcess) {

        TrackApp.getInstance().getGTM().sendGeneralEvent(
                SearchEventTracking.Event.EVENT_VIEW_SEARCH_RESULT,
                SearchEventTracking.Category.EVENT_TOP_NAV,
                String.format(SearchEventTracking.Action.NO_SEARCH_RESULT_WITH_TAB, screenName),
                String.format("keyword: %s - type: %s - alternative: %s - param: %s - treatment: %s",
                        keyword,
                        !TextUtils.isEmpty(resultCode) ? resultCode : "none/other",
                        !TextUtils.isEmpty(alternativeKeyword) ? alternativeKeyword : "none/other",
                        generateFilterEventLabel(selectedFilter),
                        !TextUtils.isEmpty(keywordProcess) ? keywordProcess : "none / other"
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

    public static void eventUserClickNewSearchOnEmptySearchProduct(String keyword) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_SEARCH_RESULT,
                EVENT_CATEGORY_SEARCH_RESULT,
                SearchEventTracking.Action.CLICK_CHANGE_KEYWORD,
                keyword
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
                DataLayer.mapOf(EVENT, SearchEventTracking.Event.PROMO_CLICK,
                        EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        EVENT_ACTION, SearchEventTracking.Action.CLICK,
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

    public static void eventActionClickCartButton(String keyword) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                SearchEventTracking.Event.CLICK_TOP_NAV,
                SearchEventTracking.Category.TOP_NAV_SEARCH_RESULT_PAGE,
                SearchEventTracking.Action.CLICK_CART_BUTTON_SEARCH_RESULT,
                keyword
        );
    }

    public static void eventActionClickHomeButton(String keyword) {
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

    public static void trackEventClickSearchBar(String keyword) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                SearchEventTracking.Event.CLICK_TOP_NAV,
                SearchEventTracking.Category.EVENT_TOP_NAV_SEARCH_SRP,
                SearchEventTracking.Action.CLICK_SEARCH_BOX,
                keyword);
    }

    public static void trackEventImpressionBannedProductsEmptySearch(String keyword) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                SearchEventTracking.Event.VIEW_SEARCH_RESULT_IRIS,
                SearchEventTracking.Category.SEARCH_RESULT,
                SearchEventTracking.Action.IMPRESSION_BANNED_PRODUCT_TICKER_EMPTY,
                keyword
        );
    }

    public static void trackEventClickGoToBrowserBannedProductsEmptySearch(String keyword) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                SearchEventTracking.Event.CLICK_SEARCH_RESULT_IRIS,
                SearchEventTracking.Category.SEARCH_RESULT,
                SearchEventTracking.Action.CLICK_BANNED_PRODUCT_TICKER_EMPTY,
                keyword
        );
    }

    public static void trackEventImpressionBannedProductsWithResult(String keyword) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                SearchEventTracking.Event.VIEW_SEARCH_RESULT_IRIS,
                SearchEventTracking.Category.SEARCH_RESULT,
                SearchEventTracking.Action.IMPRESSION_BANNED_PRODUCT_TICKER_RELATED,
                keyword
        );
    }

    public static void trackEventImpressionTicker(String keyword, int typeId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                SearchEventTracking.Event.VIEW_SEARCH_RESULT_IRIS,
                SearchEventTracking.Category.SEARCH_RESULT,
                SearchEventTracking.Action.IMPRESSION_TICKER,
                typeId + " - " + keyword
        );
    }

    public static void trackEventClickTicker(String keyword, int typeId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                SearchEventTracking.Event.SEARCH_RESULT,
                SearchEventTracking.Category.SEARCH_RESULT,
                SearchEventTracking.Action.CLICK_TICKER,
                typeId + " - " + keyword
        );
    }

    public static void trackMoEngageSearchAttempt(String query, boolean hasProductList, HashMap<String, String> category) {
        Map<String, Object> value = DataLayer.mapOf(
                SearchEventTracking.MOENGAGE.KEYWORD, query,
                SearchEventTracking.MOENGAGE.IS_RESULT_FOUND, hasProductList
        );

        if (category != null) {
            value.put(SearchEventTracking.MOENGAGE.CATEGORY_ID_MAPPING, new JSONArray(Arrays.asList(category.keySet().toArray())));
            value.put(SearchEventTracking.MOENGAGE.CATEGORY_NAME_MAPPING, new JSONArray((category.values())));
        }

        TrackApp.getInstance().getMoEngage().sendTrackEvent(value, SearchEventTracking.EventMoEngage.SEARCH_ATTEMPT);
    }

    public static void trackGTMEventSearchAttempt(GeneralSearchTrackingModel generalSearchTrackingModel) {
        Map<String, Object> value = DataLayer.mapOf(
                EVENT, SearchEventTracking.Event.CLICK_SEARCH,
                EVENT_CATEGORY, generalSearchTrackingModel.getEventCategory(),
                EVENT_ACTION, SearchEventTracking.Action.GENERAL_SEARCH,
                EVENT_LABEL, generalSearchTrackingModel.getEventLabel(),
                CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
                USER_ID, generalSearchTrackingModel.getUserId(),
                BUSINESS_UNIT, SEARCH,
                IS_RESULT_FOUND, generalSearchTrackingModel.isResultFound(),
                CATEGORY_ID_MAPPING, generalSearchTrackingModel.getCategoryIdMapping(),
                CATEGORY_NAME_MAPPING, generalSearchTrackingModel.getCategoryNameMapping(),
                RELATED_KEYWORD, generalSearchTrackingModel.getRelatedKeyword()
        );

        TrackApp.getInstance().getGTM().sendGeneralEvent(value);
    }

    public static void trackEventImpressionShopRecommendation(List<Object> shopItem, String keyword) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, SearchEventTracking.Event.PROMO_VIEW,
                        EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        EVENT_ACTION, SearchEventTracking.Action.IMPRESSION_SHOP_ALTERNATIVE,
                        EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(
                                "promoView", DataLayer.mapOf(
                                        "promotions", DataLayer.listOf(shopItem.toArray(new Object[shopItem.size()]))
                                )
                        )
                )
        );
    }

    public static void trackEventClickShopRecommendation(Object shopItem, String shopId, String keyword) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, SearchEventTracking.Event.PROMO_CLICK,
                        EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        EVENT_ACTION, SearchEventTracking.Action.CLICK_SHOP_ALTERNATIVE,
                        EVENT_LABEL, shopId + " - " + keyword,
                        ECOMMERCE, DataLayer.mapOf(
                                "promoClick", DataLayer.mapOf(
                                        "promotions", DataLayer.listOf(shopItem)
                                )
                        )
                )
        );
    }


    public static void trackEventImpressionShopRecommendationProductPreview(List<Object> shopItemProductList, String keyword) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, SearchEventTracking.Event.PRODUCT_VIEW,
                        EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        EVENT_ACTION, SearchEventTracking.Action.IMPRESSION_PRODUCT_SHOP_TAB_ALTERNATIVE,
                        EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "impressions", DataLayer.listOf(
                                        shopItemProductList.toArray(new Object[shopItemProductList.size()])
                                ))
                )
        );
    }

    public static void trackEventClickShopRecommendationProductPreview(Object shopItemProduct, String keyword) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, SearchEventTracking.Event.PRODUCT_CLICK,
                        EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        EVENT_ACTION, SearchEventTracking.Action.CLICK_PRODUCT_SHOP_TAB_ALTERNATIVE,
                        EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(
                                "click", DataLayer.mapOf(
                                        "actionField", DataLayer.mapOf("list", "/notifcenter"),
                                        "products", DataLayer.listOf(shopItemProduct)
                                )
                        )
                )
        );
    }

    public static void trackImpressionInspirationCarouselList(TrackingQueue trackingQueue, String type, String keyword, List<Object> list) {
        HashMap<String, Object> map = (HashMap<String, Object>) DataLayer.mapOf(
                EVENT, SearchEventTracking.Event.PRODUCT_VIEW,
                EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                EVENT_ACTION, SearchEventTracking.Action.IMPRESSION_INSPIRATION_CAROUSEL_PRODUCT,
                EVENT_LABEL, type + " - " + keyword,
                ECOMMERCE, DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", DataLayer.listOf(
                                list.toArray(new Object[list.size()])
                        )
                )
        );

        trackingQueue.putEETracking(
                map
        );
    }

    public static void trackImpressionInspirationCarouselInfo(TrackingQueue trackingQueue, String type, String keyword, List<Object> list) {
        HashMap<String, Object> map = (HashMap<String, Object>) DataLayer.mapOf(
                EVENT, SearchEventTracking.Event.PROMO_VIEW,
                EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                EVENT_ACTION, SearchEventTracking.Action.IMPRESSION_INSPIRATION_CAROUSEL_INFO_PRODUCT,
                EVENT_LABEL, type + " - " + keyword,
                ECOMMERCE, DataLayer.mapOf(
                        SearchEventTracking.Event.PROMO_VIEW, DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        list.toArray(new Object[list.size()])
                                )
                        )
                )
        );

        trackingQueue.putEETracking(
                map
        );
    }

    public static void trackEventClickInspirationCarouselOptionSeeAll(String type, String keywordBefore, String keywordAfter) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, SearchEventTracking.Event.SEARCH_RESULT,
                        EVENT_CATEGORY,  SearchEventTracking.Category.SEARCH_RESULT,
                        EVENT_ACTION, SearchEventTracking.Action.CLICK_INSPIRATION_CAROUSEL_SEARCH,
                        EVENT_LABEL, type + " - " + keywordBefore + " - " + keywordAfter
                )
        );
    }

    public static void trackEventClickInspirationCarouselListProduct(String type,
                                                                     String keyword,
                                                                     List<Object> products) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, SearchEventTracking.Event.PRODUCT_CLICK,
                        EVENT_CATEGORY,  SearchEventTracking.Category.SEARCH_RESULT,
                        EVENT_ACTION, SearchEventTracking.Action.CLICK_INSPIRATION_CAROUSEL_PRODUCT,
                        EVENT_LABEL, type + " - " + keyword,
                        ECOMMERCE, DataLayer.mapOf("click",
                                DataLayer.mapOf("actionField",
                                        DataLayer.mapOf("list", "/search - carousel"),
                                        "products", DataLayer.listOf(
                                                products.toArray(new Object[products.size()])
                                        )
                                )
                        )
                )
        );
    }

    public static void trackEventClickInspirationCarouselInfoProduct(String type,
                                                                     String keyword,
                                                                     List<Object> products) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, SearchEventTracking.Event.PROMO_CLICK,
                        EVENT_CATEGORY,  SearchEventTracking.Category.SEARCH_RESULT,
                        EVENT_ACTION, SearchEventTracking.Action.CLICK,
                        EVENT_LABEL, type + " - " + keyword,
                        ECOMMERCE, DataLayer.mapOf(
                                SearchEventTracking.Event.PROMO_CLICK, DataLayer.mapOf(
                                        "promotions", DataLayer.listOf(
                                                products.toArray(new Object[products.size()])
                                        )
                                )
                        )
                )
        );
    }

    public static void trackEventClickInspirationCarouselGridBanner(
            String type, String keyword, Object bannerData, String userId
    ) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, SearchEventTracking.Event.PROMO_CLICK,
                        EVENT_CATEGORY,  SearchEventTracking.Category.SEARCH_RESULT,
                        EVENT_ACTION, SearchEventTracking.Action.CLICK_INSPIRATION_CAROUSEL_GRID_BANNER,
                        EVENT_LABEL, type + " - " + keyword,
                        CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
                        BUSINESS_UNIT, SEARCH,
                        USER_ID, userId,
                        ECOMMERCE, DataLayer.mapOf(
                                SearchEventTracking.Event.PROMO_CLICK, DataLayer.mapOf(
                                        PROMOTIONS, DataLayer.listOf(bannerData)
                                )
                        )
                )
        );
    }

    public static void trackImpressionInspirationCarouselChips(
            TrackingQueue trackingQueue,
            String type,
            String keyword,
            String chipsValue,
            String userId,
            List<Object> list
    ) {
        HashMap<String, Object> map = (HashMap<String, Object>) DataLayer.mapOf(
                EVENT, SearchEventTracking.Event.PRODUCT_VIEW,
                EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                EVENT_ACTION, SearchEventTracking.Action.IMPRESSION_INSPIRATION_CAROUSEL_CHIPS_PRODUCT,
                EVENT_LABEL, type + " - " + keyword + " - " + chipsValue,
                CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
                USER_ID, userId,
                BUSINESS_UNIT, SEARCH,
                ECOMMERCE, DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", DataLayer.listOf(
                                list.toArray(new Object[list.size()])
                        )
                )
        );

        trackingQueue.putEETracking(
                map
        );
    }

    public static void trackEventClickInspirationCarouselChipsProduct(
            String type,
            String keyword,
            String chipsValue,
            String userId,
            List<Object> products
    ) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, SearchEventTracking.Event.PRODUCT_CLICK,
                        EVENT_CATEGORY,  SearchEventTracking.Category.SEARCH_RESULT,
                        EVENT_ACTION, SearchEventTracking.Action.CLICK_INSPIRATION_CAROUSEL_CHIPS_PRODUCT,
                        EVENT_LABEL, type + " - " + keyword + " - " + chipsValue,
                        CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
                        USER_ID, userId,
                        BUSINESS_UNIT, SEARCH,
                        ECOMMERCE, DataLayer.mapOf("click",
                                DataLayer.mapOf(
                                        "actionField", DataLayer.mapOf("list", "/search - carousel chips"),
                                        "products", DataLayer.listOf(
                                                products.toArray(new Object[products.size()])
                                        )
                                )
                        )
                )
        );
    }

    public static void trackEventClickInspirationCarouselChipsSeeAll(String type, String keyword, String chipsValue, String userId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                DataLayer.mapOf(EVENT, SearchEventTracking.Event.SEARCH_RESULT,
                        EVENT_CATEGORY,  SearchEventTracking.Category.SEARCH_RESULT,
                        EVENT_ACTION, SearchEventTracking.Action.CLICK_INSPIRATION_CAROUSEL_CHIPS_LIHAT_SEMUA,
                        EVENT_LABEL, type + " - " + keyword + " - " + chipsValue,
                        CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
                        USER_ID, userId,
                        BUSINESS_UNIT, SEARCH
                )
        );
    }

    public static void trackEventClickInspirationCarouselChipsVariant(String type, String keyword, String chipsValue, String userId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                DataLayer.mapOf(EVENT, SearchEventTracking.Event.SEARCH_RESULT,
                        EVENT_CATEGORY,  SearchEventTracking.Category.SEARCH_RESULT,
                        EVENT_ACTION, SearchEventTracking.Action.CLICK_INSPIRATION_CAROUSEL_CHIPS_VARIANT,
                        EVENT_LABEL, type + " - " + keyword + " - " + chipsValue,
                        CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
                        USER_ID, userId,
                        BUSINESS_UNIT, SEARCH
                )
        );
    }

    public static void trackEventImpressionBroadMatch(TrackingQueue trackingQueue, String keyword, String alternativeKeyword, String userId, List<Object> broadMatchItems) {
        HashMap<String, Object> map = (HashMap<String, Object>) DataLayer.mapOf(
                EVENT, SearchEventTracking.Event.PRODUCT_VIEW,
                EVENT_CATEGORY,  SearchEventTracking.Category.SEARCH_RESULT,
                EVENT_ACTION, SearchEventTracking.Action.IMPRESSION_BROAD_MATCH,
                EVENT_LABEL, String.format("%s - %s", keyword, alternativeKeyword),
                CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
                BUSINESS_UNIT, SEARCH,
                USER_ID, userId,
                ECOMMERCE, DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", DataLayer.listOf(
                                broadMatchItems.toArray(new Object[broadMatchItems.size()])
                        ))
        );

        trackingQueue.putEETracking(
                map
        );
    }

    public static void trackEventClickBroadMatchSeeMore(String keyword, String alternativeKeyword) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT, SearchEventTracking.Event.SEARCH_RESULT,
                        EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        EVENT_ACTION, SearchEventTracking.Action.CLICK_BROAD_MATCH_LIHAT_SEMUA,
                        EVENT_LABEL, String.format("%s - %s", keyword, alternativeKeyword)
                )
        );
    }

    public static void trackEventClickBroadMatchItem(String keyword, String alternativeKeyword, String userId, List<Object> broadMatchItems) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, SearchEventTracking.Event.PRODUCT_CLICK,
                        EVENT_CATEGORY,  SearchEventTracking.Category.SEARCH_RESULT,
                        EVENT_ACTION, SearchEventTracking.Action.CLICK_BROAD_MATCH,
                        EVENT_LABEL, String.format("%s - %s", keyword, alternativeKeyword),
                        CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
                        BUSINESS_UNIT, SEARCH,
                        USER_ID, userId,
                        ECOMMERCE, DataLayer.mapOf("click",
                                DataLayer.mapOf("actionField",
                                        DataLayer.mapOf("list", "/search - broad match"),
                                        "products", DataLayer.listOf(
                                                broadMatchItems.toArray(new Object[broadMatchItems.size()])
                                        )
                                )
                        )
                )
        );
    }

    public static void trackEventClickInspirationCardOption(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
            SearchEventTracking.Event.SEARCH_RESULT,
            SearchEventTracking.Category.SEARCH_RESULT,
            SearchEventTracking.Action.CLICK_INSPIRATION_CARD,
            label
        );
    }

    public static void trackEventAddToCart(String keyword, boolean isOrganicAds, Object productItem) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.ADDTOCART,
                        TrackAppUtils.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        TrackAppUtils.EVENT_ACTION, SearchEventTracking.Action.CLICK_ADD_TO_CART_ON_PRODUCT_OPTIONS,
                        TrackAppUtils.EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(
                                SearchEventTracking.ECommerce.CURRENCY_CODE, SearchEventTracking.ECommerce.IDR,
                                SearchEventTracking.ECommerce.ADD, DataLayer.mapOf(
                                        SearchEventTracking.ECommerce.ACTION_FIELD, DataLayer.mapOf(
                                                "list", getActionFieldString(isOrganicAds)
                                        ),
                                        SearchEventTracking.ECommerce.PRODUCTS, DataLayer.listOf(productItem)
                                )
                        )
                )
        );
    }

    public static void trackEventGoToShopPage(String keyword, Object item) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, SearchEventTracking.Event.PROMO_CLICK,
                        TrackAppUtils.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        TrackAppUtils.EVENT_ACTION, SearchEventTracking.Action.CLICK,
                        TrackAppUtils.EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(
                                PROMO_CLICK, DataLayer.mapOf(
                                        PROMOTIONS, DataLayer.listOf(item)
                                )
                        )
                )
        );
    }

    public static void trackEventShareProduct(String queryKey, String productId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, SearchEventTracking.Event.SEARCH_RESULT,
                        TrackAppUtils.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        TrackAppUtils.EVENT_ACTION, SearchEventTracking.Action.CLICK_SHARE_PRODUCT_OPTIONS,
                        TrackAppUtils.EVENT_LABEL, queryKey + " - " + productId
                )
        );
    }
}
