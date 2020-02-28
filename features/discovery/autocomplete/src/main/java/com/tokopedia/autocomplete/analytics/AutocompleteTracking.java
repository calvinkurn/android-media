package com.tokopedia.autocomplete.analytics;

import android.content.Context;

import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.autocomplete.initialstate.BaseItemInitialStateSearch;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import static com.tokopedia.autocomplete.analytics.AutocompleteTrackingConstant.CLICK_CARI;
import static com.tokopedia.autocomplete.analytics.AutocompleteTrackingConstant.LONG_CLICK;
import static com.tokopedia.autocomplete.analytics.AutocompleteTrackingConstant.LONG_PRESS;
import static com.tokopedia.autocomplete.analytics.AutocompleteTrackingConstant.PRODUCT_SEARCH;
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

    public static final String EVENTCATEGORY_TOP_NAV = "top nav";

    public static final String CLICK_POPULAR_SEARCH = "click - popular search";
    public static final String CLICK_RECENT_SEARCH = "click - recent search";
    public static final String CLICK_DIGITAL_PRODUCT_SUGGESTION = "click - digital product suggestion";
    public static final String CLICK_CATEGORY_SUGGESTION = "click - category suggestion";
    public static final String CLICK_SEARCH = "click - search";
    public static final String CLICK_PROFILE_SUGGESTION = "click - profile autocomplete on suggestion list";
    public static final String CLICK_TOP_PROFILE_SUGGESTION = "click - profile autocomplete on top suggestion";
    public static final String CLICK_REFRESH_POPULAR_SEARCH = "click refresh on popular search";

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

    public static void eventClickPopularSearch(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_SEARCH,
                EVENTCATEGORY_TOP_NAV,
                CLICK_POPULAR_SEARCH,
                label
        );
    }

    public static void eventClickRecentSearch(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_SEARCH,
                EVENTCATEGORY_TOP_NAV,
                CLICK_RECENT_SEARCH,
                label
        );
    }

    public static void eventClickAutoCompleteSearch(Context context, String label, String tabName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_SEARCH_RESULT,
                EVENTCATEGORY_TOP_NAV,
                String.format("click - product autocomplete - tab: %s", tabName),
                label
        );
    }

    public static void eventClickShopSearch(Context context,
                                            String label,
                                            String tabName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_SEARCH_RESULT,
                EVENTCATEGORY_TOP_NAV,
                String.format("click - shop autocomplete - tab: %s", tabName),
                label
        );
    }

    public static void eventClickInCategory(Context context,
                                            String label,
                                            String tabName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_SEARCH_RESULT,
                EVENTCATEGORY_TOP_NAV,
                String.format("click - category autocomplete - tab: %s", tabName),
                label
        );
    }

    public static void eventClickInHotlist(Context context,
                                           String keyword,
                                           String hotlistName,
                                           String id,
                                           int position,
                                           String applink) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_TOP_NAV,
                EVENTCATEGORY_TOP_NAV,
                ACTION_CLICK_HOTLIST_SUGGESTION,
                String.format(
                        LABEL_HOTLIST_CLICK,
                        keyword,
                        hotlistName,
                        id,
                        String.valueOf(position),
                        applink
                )
        );
    }

    public static void eventClickCategory(Context context,
                                          String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_SEARCH_RESULT,
                EVENTCATEGORY_TOP_NAV,
                CLICK_CATEGORY_SUGGESTION,
                label
        );
    }

    public static void eventClickDigital(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_SEARCH_RESULT,
                EVENTCATEGORY_TOP_NAV,
                CLICK_DIGITAL_PRODUCT_SUGGESTION,
                label
        );
    }

    public static void eventClickProfile(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_TOP_NAV,
                EVENTCATEGORY_TOP_NAV,
                CLICK_PROFILE_SUGGESTION,
                label
        );
    }

    public static void eventClickTopProfile(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_TOP_NAV,
                EVENTCATEGORY_TOP_NAV,
                CLICK_TOP_PROFILE_SUGGESTION,
                label
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

    public static void eventClickRefreshPopularSearch(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_TOP_NAV,
                EVENTCATEGORY_TOP_NAV + " - homepage",
                CLICK_REFRESH_POPULAR_SEARCH,
                ""
        );
    }
}
