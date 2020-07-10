package com.tokopedia.autocomplete.analytics;

import android.content.Context;

import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.autocomplete.initialstate.BaseItemInitialStateSearch;
import com.tokopedia.iris.Iris;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.tokopedia.autocomplete.analytics.AutocompleteTrackingConstant.BUSINESS_UNIT;
import static com.tokopedia.autocomplete.analytics.AutocompleteTrackingConstant.CLICK_CARI;
import static com.tokopedia.autocomplete.analytics.AutocompleteTrackingConstant.CURRENT_SITE;
import static com.tokopedia.autocomplete.analytics.AutocompleteTrackingConstant.LONG_CLICK;
import static com.tokopedia.autocomplete.analytics.AutocompleteTrackingConstant.LONG_PRESS;
import static com.tokopedia.autocomplete.analytics.AutocompleteTrackingConstant.PRODUCT_SEARCH;
import static com.tokopedia.autocomplete.analytics.AutocompleteTrackingConstant.SCREEN_NAME;
import static com.tokopedia.autocomplete.analytics.AutocompleteTrackingConstant.USER_ID;

public class AutocompleteTracking {

    private UserSessionInterface userSessionInterface;

    @Inject
    public AutocompleteTracking(UserSessionInterface userSessionInterface) {
        this.userSessionInterface = userSessionInterface;
    }

    public void eventSearchShortcut() {
        Map<String, Object> eventTracking = new HashMap<>();
        eventTracking.put(AutocompleteTrackingConstant.EVENT, LONG_CLICK);
        eventTracking.put(AutocompleteTrackingConstant.EVENT_CATEGORY, LONG_PRESS);
        eventTracking.put(AutocompleteTrackingConstant.EVENT_ACTION, CLICK_CARI);
        eventTracking.put(AutocompleteTrackingConstant.EVENT_LABEL, PRODUCT_SEARCH);
        eventTracking.put(USER_ID, userSessionInterface.isLoggedIn() ? userSessionInterface.getUserId() : "0");

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(eventTracking);
    }

    public static final String EVENT = "event";
    public static final String EVENT_CLICK_TOP_NAV = "clickTopNav";
    public static final String EVENT_CATEGORY = "eventCategory";
    public static final String EVENT_ACTION = "eventAction";
    public static final String EVENT_LABEL = "eventLabel";
    public static final String EVENT_CLICK_SEARCH = "clickSearch";
    public static final String EVENT_CLICK_SEARCH_RESULT = "clickSearchResult";
    public static final String EVENT_IMPRESSED_INITIAL_STATE_PRODUCT = "productViewIris";
    public static final String EVENT_IMPRESSED_INITIAL_STATE_PROMO = "promoViewIris";

    public static final String EVENTCATEGORY_TOP_NAV = "top nav";
    public static final String EVENT_CATEGORY_INITIAL_STATE = "initial-state";

    public static final String CLICK_POPULAR_SEARCH = "click - popular search";
    public static final String CLICK_RECENT_SEARCH = "click - recent search";
    public static final String CLICK_DIGITAL_PRODUCT_SUGGESTION = "click - digital product suggestion";
    public static final String CLICK_CATEGORY_SUGGESTION = "click - category suggestion";
    public static final String CLICK_SEARCH = "click - search";
    public static final String CLICK_PROFILE_SUGGESTION = "click - profile autocomplete on suggestion list";
    public static final String CLICK_TOP_PROFILE_SUGGESTION = "click - profile autocomplete on top suggestion";
    public static final String CLICK_REFRESH_POPULAR_SEARCH = "click refresh on popular search";
    public static final String CLICK_SHOP_SUGGESTION = "click - shop autocomplete";
    public static final String CLICK_KEYWORD_SUGGESTION = "click - product autocomplete";
    public static final String EVENT_ACTION_CLICK_RECENT_SEARCH_AUTOCOMPLETE = "click - recent search autocomplete";
    public static final String CLICK_RECENT_SHOP = "click - shop - recent search";
    public static final String EVENT_ACTION_IMPRESSED_RECENT_VIEW = "impression - recent view product";
    public static final String EVENT_ACTION_IMPRESSED_RECENT_SEARCH = "impression - recent search";
    public static final String EVENT_ACTION_IMPRESSED_POPULAR_SEARCH = "impression - popular search";
    public static final String EVENT_ACTION_TOP_SHOP = "click - shop - carousel";
    public static final String EVENT_ACTION_TOP_SHOP_SEE_MORE = "click - lihat toko lainnya - carousel";

    public static final String ECOMMERCE = "ecommerce";
    public static final String PRODUCT_CLICK = "productClick";
    public static final String CLICK_RECENT_VIEW_PRODUCT = "click - recent view product";
    public static final String CLICK = "click";
    public static final String ACTION_FIELD = "actionField";
    public static final String LIST = "list";
    public static final String RECENT_VIEW_ACTION_FIELD = "/search - recentview - product";
    public static final String PRODUCTS = "products";
    public static final String PRODUCT_NAME = "name";
    public static final String PRODUCT_ID = "id";
    public static final String PRODUCT_PRICE = "price";
    public static final String PRODUCT_BRAND = "brand";
    public static final String PRODUCT_CATEGORY = "category";
    public static final String PRODUCT_VARIANT = "variant";
    public static final String PRODUCT_POSITION = "position";
    public static final String NONE_OTHER = "none / other";
    private static final String LABEL_RECENT_VIEW_CLICK = "po: %s - applink: %s";
    private static final String LABEL_HOTLIST_CLICK = "keyword: %s - hotlist: %s - hotlist id: %s - po: %s - applink: %s";
    public static final String ACTION_CLICK_HOTLIST_SUGGESTION = "click - hotlist suggestion";
    public static final String CAMPAIGN_CODE = "campaignCode";

    public static final String CURRENT_SITE_TKPD = "tokopediamarketplace";
    public static final String BUSINESS_UNIT_SEARCH = "search";


    public static void eventClickPopularSearch(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_SEARCH,
                EVENTCATEGORY_TOP_NAV,
                CLICK_POPULAR_SEARCH,
                label
        );
    }

    public static void eventClickRecentSearch(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_SEARCH,
                EVENTCATEGORY_TOP_NAV,
                CLICK_RECENT_SEARCH,
                label
        );
    }

    public static void eventClickShop(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_SEARCH_RESULT,
                EVENTCATEGORY_TOP_NAV,
                CLICK_SHOP_SUGGESTION,
                label
        );
    }

    public static void eventClickProfile(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_TOP_NAV,
                EVENTCATEGORY_TOP_NAV,
                CLICK_PROFILE_SUGGESTION,
                label
        );
    }

    public static void eventClickKeyword(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_SEARCH_RESULT,
                EVENTCATEGORY_TOP_NAV,
                CLICK_KEYWORD_SUGGESTION,
                label
        );
    }

    public static void eventClickCurated(String label, String campaignCode) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, EVENT_CLICK_SEARCH_RESULT,
                        EVENT_CATEGORY, EVENTCATEGORY_TOP_NAV + " - /",
                        EVENT_ACTION, CLICK_DIGITAL_PRODUCT_SUGGESTION,
                        EVENT_LABEL, label,
                        CAMPAIGN_CODE, campaignCode
                )
        );
    }

    public static void eventClickSubmit(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_SEARCH,
                EVENTCATEGORY_TOP_NAV,
                CLICK_SEARCH,
                label
        );
    }

    public static void eventClickRecentView(String position,
                                            BaseItemInitialStateSearch data) {
        Map<String, Object> productData = convertSearchItemToProductData(data, position);
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, PRODUCT_CLICK,
                        EVENT_CATEGORY, EVENTCATEGORY_TOP_NAV,
                        EVENT_ACTION, CLICK_RECENT_VIEW_PRODUCT,
                        EVENT_LABEL, String.
                                format(LABEL_RECENT_VIEW_CLICK,
                                        position,
                                        data.getApplink()),
                        ECOMMERCE, DataLayer.mapOf(
                                CLICK,
                                DataLayer.mapOf(
                                        ACTION_FIELD, DataLayer.mapOf(LIST, RECENT_VIEW_ACTION_FIELD),
                                        PRODUCTS, DataLayer.listOf(
                                                productData
                                        )
                                )
                        )
                )
        );
    }

    private static Map<String, Object> convertSearchItemToProductData(BaseItemInitialStateSearch data,
                                                                      String position) {
        return DataLayer.mapOf(
                PRODUCT_NAME, data.getTitle(),
                PRODUCT_ID, data.getProductId(),
                PRODUCT_PRICE, "",
                PRODUCT_BRAND, NONE_OTHER,
                PRODUCT_CATEGORY, NONE_OTHER,
                PRODUCT_VARIANT, NONE_OTHER,
                PRODUCT_POSITION, position
        );
    }

    public static void eventClickRefreshPopularSearch() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_TOP_NAV,
                EVENTCATEGORY_TOP_NAV + " - homepage",
                CLICK_REFRESH_POPULAR_SEARCH,
                ""
        );
    }

    public static void eventClickRecentKeyword(String keyword) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_TOP_NAV,
                EVENTCATEGORY_TOP_NAV + " - homepage",
                EVENT_ACTION_CLICK_RECENT_SEARCH_AUTOCOMPLETE,
                keyword
        );
    }

    public static void eventClickRecentShop(String label, String userId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
            DataLayer.mapOf(
                    EVENT, EVENT_CLICK_TOP_NAV,
                    EVENT_CATEGORY, EVENTCATEGORY_TOP_NAV + " - /",
                    EVENT_ACTION, CLICK_RECENT_SHOP,
                    EVENT_LABEL, label,
                    SCREEN_NAME, "/",
                    CURRENT_SITE, CURRENT_SITE_TKPD,
                    USER_ID, userId,
                    BUSINESS_UNIT, BUSINESS_UNIT_SEARCH
            )
        );
    }

    public static void impressedRecentView(Iris iris, List<Object> list) {
        HashMap<String, Object> map = (HashMap<String, Object>) DataLayer.mapOf(
                EVENT, EVENT_IMPRESSED_INITIAL_STATE_PRODUCT,
                EVENT_CATEGORY, EVENTCATEGORY_TOP_NAV + " - " + EVENT_CATEGORY_INITIAL_STATE,
                EVENT_ACTION, EVENT_ACTION_IMPRESSED_RECENT_VIEW,
                EVENT_LABEL, "",
                "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", DataLayer.listOf(
                                list.toArray(new Object[list.size()])
                        ))
        );
        iris.saveEvent(map);
    }

    public static void impressedRecentSearch(Iris iris, List<Object> list, String keyword) {
        HashMap<String, Object> map = (HashMap<String, Object>) DataLayer.mapOf(
                EVENT, EVENT_IMPRESSED_INITIAL_STATE_PROMO,
                EVENT_CATEGORY, EVENTCATEGORY_TOP_NAV + " - " + EVENT_CATEGORY_INITIAL_STATE,
                EVENT_ACTION, EVENT_ACTION_IMPRESSED_RECENT_SEARCH,
                EVENT_LABEL, keyword,
                "ecommerce", DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        list.toArray(new Object[list.size()])
                                )
                        )
                )
        );
        iris.saveEvent(map);
    }

    public static void impressedPopularSearch(Iris iris, List<Object> list) {
        HashMap<String, Object> map = (HashMap<String, Object>) DataLayer.mapOf(
                EVENT, EVENT_IMPRESSED_INITIAL_STATE_PROMO,
                EVENT_CATEGORY, EVENTCATEGORY_TOP_NAV + " - " + EVENT_CATEGORY_INITIAL_STATE,
                EVENT_ACTION, EVENT_ACTION_IMPRESSED_POPULAR_SEARCH,
                EVENT_LABEL, "",
                "ecommerce", DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        list.toArray(new Object[list.size()])
                                )
                        )
                )
        );
        iris.saveEvent(map);
    }

    public static void eventClickTopShop(String label) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        EVENT, EVENT_CLICK_TOP_NAV,
                        EVENT_CATEGORY, EVENTCATEGORY_TOP_NAV + " - /",
                        EVENT_ACTION, EVENT_ACTION_TOP_SHOP,
                        EVENT_LABEL, label
                )
        );
    }

    public static void eventClickTopShopSeeMore(String label) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        EVENT, EVENT_CLICK_TOP_NAV,
                        EVENT_CATEGORY, EVENTCATEGORY_TOP_NAV + " - /",
                        EVENT_ACTION, EVENT_ACTION_TOP_SHOP_SEE_MORE,
                        EVENT_LABEL, label
                )
        );
    }
}
