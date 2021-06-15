package com.tokopedia.autocomplete.analytics;

import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateItemTrackingModel;
import com.tokopedia.iris.Iris;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.tokopedia.autocomplete.analytics.AutocompleteTrackingConstant.ACTION_FIELD;
import static com.tokopedia.autocomplete.analytics.AutocompleteTrackingConstant.BUSINESS_UNIT;
import static com.tokopedia.autocomplete.analytics.AutocompleteTrackingConstant.CAMPAIGN_CODE;
import static com.tokopedia.autocomplete.analytics.AutocompleteTrackingConstant.CLICK;
import static com.tokopedia.autocomplete.analytics.AutocompleteTrackingConstant.CURRENT_SITE;
import static com.tokopedia.autocomplete.analytics.AutocompleteTrackingConstant.ECOMMERCE;
import static com.tokopedia.autocomplete.analytics.AutocompleteTrackingConstant.EVENT;
import static com.tokopedia.autocomplete.analytics.AutocompleteTrackingConstant.EVENT_ACTION;
import static com.tokopedia.autocomplete.analytics.AutocompleteTrackingConstant.EVENT_CATEGORY;
import static com.tokopedia.autocomplete.analytics.AutocompleteTrackingConstant.EVENT_LABEL;
import static com.tokopedia.autocomplete.analytics.AutocompleteTrackingConstant.LIST;
import static com.tokopedia.autocomplete.analytics.AutocompleteTrackingConstant.PAGE_SOURCE;
import static com.tokopedia.autocomplete.analytics.AutocompleteTrackingConstant.PRODUCTS;
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
        eventTracking.put(EVENT, AutocompleteEventTracking.Event.LONG_CLICK);
        eventTracking.put(EVENT_CATEGORY, AutocompleteEventTracking.Category.LONG_PRESS);
        eventTracking.put(EVENT_ACTION, AutocompleteEventTracking.Action.CLICK_CARI);
        eventTracking.put(EVENT_LABEL, AutocompleteEventTracking.Label.PRODUCT_SEARCH);
        eventTracking.put(USER_ID, userSessionInterface.isLoggedIn() ? userSessionInterface.getUserId() : "0");

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(eventTracking);
    }

    public static void eventClickRecentSearch(String label, String pageSource) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
            DataLayer.mapOf(
                    EVENT, AutocompleteEventTracking.Event.CLICK_SEARCH,
                    EVENT_CATEGORY, AutocompleteEventTracking.Category.TOP_NAV,
                    EVENT_ACTION, AutocompleteEventTracking.Action.CLICK_RECENT_SEARCH,
                    EVENT_LABEL, label,
                    CURRENT_SITE, AutocompleteEventTracking.Iris.TOKOPEDIA_MARKETPLACE,
                    BUSINESS_UNIT, AutocompleteEventTracking.Iris.SEARCH,
                    PAGE_SOURCE, pageSource
            )
        );
    }

    public static void eventClickShop(String label, String pageSource) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT, AutocompleteEventTracking.Event.CLICK_TOP_NAV,
                        EVENT_CATEGORY, AutocompleteEventTracking.Category.TOP_NAV,
                        EVENT_ACTION, AutocompleteEventTracking.Action.CLICK_SHOP_SUGGESTION,
                        EVENT_LABEL, label,
                        CURRENT_SITE, AutocompleteEventTracking.Iris.TOKOPEDIA_MARKETPLACE,
                        BUSINESS_UNIT, AutocompleteEventTracking.Iris.SEARCH,
                        PAGE_SOURCE, pageSource
                )
        );
    }

    public static void eventClickProfile(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                AutocompleteEventTracking.Event.CLICK_TOP_NAV,
                AutocompleteEventTracking.Category.TOP_NAV,
                AutocompleteEventTracking.Action.CLICK_PROFILE_SUGGESTION,
                label
        );
    }

    public static void eventClickKeyword(String label, String pageSource) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT, AutocompleteEventTracking.Event.CLICK_TOP_NAV,
                        EVENT_CATEGORY, AutocompleteEventTracking.Category.TOP_NAV,
                        EVENT_ACTION, AutocompleteEventTracking.Action.CLICK_KEYWORD_SUGGESTION,
                        EVENT_LABEL, label,
                        CURRENT_SITE, AutocompleteEventTracking.Iris.TOKOPEDIA_MARKETPLACE,
                        BUSINESS_UNIT, AutocompleteEventTracking.Iris.SEARCH,
                        PAGE_SOURCE, pageSource
                )
        );
    }

    public static void eventClickCurated(String label, String campaignCode, String pageSource) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, AutocompleteEventTracking.Event.CLICK_TOP_NAV,
                        EVENT_CATEGORY, AutocompleteEventTracking.Category.TOP_NAV + " - /",
                        EVENT_ACTION, AutocompleteEventTracking.Action.CLICK_DIGITAL_PRODUCT_SUGGESTION,
                        EVENT_LABEL, label,
                        CAMPAIGN_CODE, campaignCode,
                        CURRENT_SITE, AutocompleteEventTracking.Iris.TOKOPEDIA_MARKETPLACE,
                        BUSINESS_UNIT, AutocompleteEventTracking.Iris.SEARCH,
                        PAGE_SOURCE, pageSource
                )
        );
    }

    public static void eventClickSubmit(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                AutocompleteEventTracking.Event.CLICK_SEARCH,
                AutocompleteEventTracking.Category.TOP_NAV,
                AutocompleteEventTracking.Action.CLICK_SEARCH,
                label
        );
    }

    public static void eventClickRecentView(Object productDataLayer, String label) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, AutocompleteEventTracking.Event.PRODUCT_CLICK,
                        EVENT_CATEGORY, AutocompleteEventTracking.Category.TOP_NAV,
                        EVENT_ACTION, AutocompleteEventTracking.Action.CLICK_RECENT_VIEW_PRODUCT,
                        EVENT_LABEL, label,
                        ECOMMERCE, DataLayer.mapOf(
                                CLICK,
                                DataLayer.mapOf(
                                        ACTION_FIELD, DataLayer.mapOf(LIST, AutocompleteEventTracking.Other.RECENT_VIEW_ACTION_FIELD),
                                        PRODUCTS, DataLayer.listOf(
                                                productDataLayer
                                        )
                                )
                        )
                )
        );
    }

    public static void eventClickRefreshPopularSearch() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                AutocompleteEventTracking.Event.CLICK_TOP_NAV,
                AutocompleteEventTracking.Category.TOP_NAV + " - homepage",
                AutocompleteEventTracking.Action.CLICK_REFRESH_POPULAR_SEARCH,
                ""
        );
    }

    public static void eventClickRecentKeyword(String keyword, String pageSource) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT, AutocompleteEventTracking.Event.CLICK_TOP_NAV,
                        EVENT_CATEGORY, AutocompleteEventTracking.Category.TOP_NAV + " - homepage",
                        EVENT_ACTION, AutocompleteEventTracking.Action.CLICK_RECENT_SEARCH_AUTOCOMPLETE,
                        EVENT_LABEL, keyword,
                        CURRENT_SITE, AutocompleteEventTracking.Iris.TOKOPEDIA_MARKETPLACE,
                        BUSINESS_UNIT, AutocompleteEventTracking.Iris.SEARCH,
                        PAGE_SOURCE, pageSource
                )
        );
    }

    public static void eventClickRecentShop(String label, String userId, String pageSource) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
            DataLayer.mapOf(
                    EVENT, AutocompleteEventTracking.Event.CLICK_TOP_NAV,
                    EVENT_CATEGORY, AutocompleteEventTracking.Category.TOP_NAV + " - /",
                    EVENT_ACTION, AutocompleteEventTracking.Action.CLICK_RECENT_SHOP,
                    EVENT_LABEL, label,
                    SCREEN_NAME, "/",
                    CURRENT_SITE, AutocompleteEventTracking.Iris.TOKOPEDIA_MARKETPLACE,
                    USER_ID, userId,
                    BUSINESS_UNIT, AutocompleteEventTracking.Iris.SEARCH,
                    PAGE_SOURCE, pageSource
            )
        );
    }

    public static void impressedRecentView(Iris iris, List<Object> list) {
        HashMap<String, Object> map = (HashMap<String, Object>) DataLayer.mapOf(
                EVENT, AutocompleteEventTracking.Event.PRODUCT_VIEW_IRIS,
                EVENT_CATEGORY, AutocompleteEventTracking.Category.TOP_NAV + " - " + AutocompleteEventTracking.Category.INITIAL_STATE,
                EVENT_ACTION, AutocompleteEventTracking.Action.IMPRESSION_RECENT_VIEW,
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
                EVENT, AutocompleteEventTracking.Event.PROMO_VIEW_IRIS,
                EVENT_CATEGORY, AutocompleteEventTracking.Category.TOP_NAV + " - " + AutocompleteEventTracking.Category.INITIAL_STATE,
                EVENT_ACTION, AutocompleteEventTracking.Action.IMPRESSION_RECENT_SEARCH,
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

    public static void eventClickTopShop(String label) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        EVENT, AutocompleteEventTracking.Event.CLICK_TOP_NAV,
                        EVENT_CATEGORY, AutocompleteEventTracking.Category.TOP_NAV + " - /",
                        EVENT_ACTION, AutocompleteEventTracking.Action.CLICK_TOP_SHOP,
                        EVENT_LABEL, label
                )
        );
    }

    public static void eventClickTopShopSeeMore(String label) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        EVENT, AutocompleteEventTracking.Event.CLICK_TOP_NAV,
                        EVENT_CATEGORY, AutocompleteEventTracking.Category.TOP_NAV + " - /",
                        EVENT_ACTION, AutocompleteEventTracking.Action.CLICK_TOP_SHOP_SEE_MORE,
                        EVENT_LABEL, label
                )
        );
    }

    public static void impressedSeeMoreRecentSearch(Iris iris, String userId) {
        HashMap<String, Object> map = (HashMap<String, Object>) DataLayer.mapOf(
                EVENT, AutocompleteEventTracking.Event.VIEW_TOP_NAV_IRIS,
                EVENT_CATEGORY, AutocompleteEventTracking.Category.TOP_NAV + " - /",
                EVENT_ACTION, AutocompleteEventTracking.Action.IMPRESSION_SEE_MORE_RECENT_SEARCH,
                EVENT_LABEL, "",
                BUSINESS_UNIT, AutocompleteEventTracking.Iris.SEARCH,
                CURRENT_SITE, AutocompleteEventTracking.Iris.TOKOPEDIA_MARKETPLACE,
                USER_ID, userId
        );
        iris.saveEvent(map);
    }

    public static void eventClickSeeMoreRecentSearch(String userId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT, AutocompleteEventTracking.Event.CLICK_TOP_NAV,
                        EVENT_CATEGORY, AutocompleteEventTracking.Category.TOP_NAV + " - /",
                        EVENT_ACTION, AutocompleteEventTracking.Action.CLICK_SEE_MORE_RECENT_SEARCH,
                        EVENT_LABEL, "",
                        BUSINESS_UNIT, AutocompleteEventTracking.Iris.SEARCH,
                        CURRENT_SITE, AutocompleteEventTracking.Iris.TOKOPEDIA_MARKETPLACE,
                        USER_ID, userId
                )
        );
    }

    public static void impressedDynamicSection(Iris iris, DynamicInitialStateItemTrackingModel model) {
        HashMap<String, Object> map = (HashMap<String, Object>) DataLayer.mapOf(
                EVENT, AutocompleteEventTracking.Event.PROMO_VIEW_IRIS,
                EVENT_CATEGORY, AutocompleteEventTracking.Category.TOP_NAV + " - /",
                EVENT_ACTION, AutocompleteEventTracking.Action.IMPRESSION_POPULAR_SEARCH + " - " + model.getType(),
                EVENT_LABEL, "title: " + model.getTitle(),
                BUSINESS_UNIT, AutocompleteEventTracking.Iris.SEARCH,
                CURRENT_SITE, AutocompleteEventTracking.Iris.TOKOPEDIA_MARKETPLACE,
                USER_ID, model.getUserId(),
                "ecommerce", DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        model.getList().toArray(new Object[model.getList().size()])
                                )
                        )
                )
        );
        iris.saveEvent(map);
    }

    public static void eventClickDynamicSection(String userId, String label, String type, String pageSource) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT, AutocompleteEventTracking.Event.CLICK_TOP_NAV,
                        EVENT_CATEGORY, AutocompleteEventTracking.Category.TOP_NAV + " - /",
                        EVENT_ACTION, AutocompleteEventTracking.Action.CLICK_DYNAMIC_SECTION + " - " + type,
                        EVENT_LABEL, label,
                        BUSINESS_UNIT, AutocompleteEventTracking.Iris.SEARCH,
                        CURRENT_SITE, AutocompleteEventTracking.Iris.TOKOPEDIA_MARKETPLACE,
                        USER_ID, userId,
                        PAGE_SOURCE, pageSource
                )
        );
    }

    public static void eventClickLocalKeyword(String label, String userId, String pageSource) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT, AutocompleteEventTracking.Event.CLICK_TOP_NAV,
                        EVENT_CATEGORY, AutocompleteEventTracking.Category.TOP_NAV + " - /",
                        EVENT_ACTION, AutocompleteEventTracking.Action.CLICK_LOCAL_KEYWORD,
                        EVENT_LABEL, label,
                        BUSINESS_UNIT, AutocompleteEventTracking.Iris.SEARCH,
                        CURRENT_SITE, AutocompleteEventTracking.Iris.TOKOPEDIA_MARKETPLACE,
                        USER_ID, userId,
                        PAGE_SOURCE, pageSource
                )
        );
    }

    public static void eventClickGlobalKeyword(String label, String userId, String pageSource) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT, AutocompleteEventTracking.Event.CLICK_TOP_NAV,
                        EVENT_CATEGORY, AutocompleteEventTracking.Category.TOP_NAV + " - /",
                        EVENT_ACTION, AutocompleteEventTracking.Action.CLICK_GLOBAL_KEYWORD,
                        EVENT_LABEL, label,
                        BUSINESS_UNIT, AutocompleteEventTracking.Iris.SEARCH,
                        CURRENT_SITE, AutocompleteEventTracking.Iris.TOKOPEDIA_MARKETPLACE,
                        USER_ID, userId,
                        PAGE_SOURCE, pageSource
                )
        );
    }

    public static void eventClickCuratedCampaignCard(String userId, String label, String type) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT, AutocompleteEventTracking.Event.CLICK_TOP_NAV,
                        EVENT_CATEGORY, AutocompleteEventTracking.Category.TOP_NAV_INITIAL_STATE + " - /",
                        EVENT_ACTION, AutocompleteEventTracking.Action.CLICK_CURATED_CAMPAIGN + " - " + type,
                        EVENT_LABEL, label,
                        BUSINESS_UNIT, AutocompleteEventTracking.Iris.SEARCH,
                        CURRENT_SITE, AutocompleteEventTracking.Iris.TOKOPEDIA_MARKETPLACE,
                        USER_ID, userId
                )
        );
    }

    public static void impressedCuratedCampaign(Iris iris, String userId, String label, String type) {
        HashMap<String, Object> map = (HashMap<String, Object>) DataLayer.mapOf(
                EVENT, AutocompleteEventTracking.Event.VIEW_TOP_NAV_IRIS,
                EVENT_CATEGORY, AutocompleteEventTracking.Category.TOP_NAV_INITIAL_STATE + " - /",
                EVENT_ACTION, AutocompleteEventTracking.Action.IMPRESSION_CURATED_CAMPAIGN + " - " + type,
                EVENT_LABEL, label,
                BUSINESS_UNIT, AutocompleteEventTracking.Iris.SEARCH,
                CURRENT_SITE, AutocompleteEventTracking.Iris.TOKOPEDIA_MARKETPLACE,
                USER_ID, userId
        );
        iris.saveEvent(map);
    }

    public static void eventClickInitialStateProductLine(Object productDataLayer, String userId, String label, String pageSource) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        EVENT, AutocompleteEventTracking.Event.PRODUCT_CLICK,
                        EVENT_CATEGORY, AutocompleteEventTracking.Category.TOP_NAV,
                        EVENT_ACTION, AutocompleteEventTracking.Action.CLICK_INITIAL_STATE_PRODUCT_LINE,
                        EVENT_LABEL, label,
                        ECOMMERCE, DataLayer.mapOf(
                                CLICK, DataLayer.mapOf(
                                        ACTION_FIELD, DataLayer.mapOf(LIST, AutocompleteEventTracking.Other.PRODUCT_LINE_INITIAL_STATE_ACTION_FIELD),
                                        PRODUCTS, DataLayer.listOf(
                                                productDataLayer
                                        )
                                )
                        ),
                        BUSINESS_UNIT, AutocompleteEventTracking.Iris.SEARCH,
                        CURRENT_SITE, AutocompleteEventTracking.Iris.TOKOPEDIA_MARKETPLACE,
                        USER_ID, userId,
                        PAGE_SOURCE, pageSource
                )
        );
    }

    public static void eventClickSuggestionProductLine(Object productDataLayer, String label, String userId) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        EVENT, AutocompleteEventTracking.Event.PRODUCT_CLICK,
                        EVENT_CATEGORY, AutocompleteEventTracking.Category.TOP_NAV,
                        EVENT_ACTION, AutocompleteEventTracking.Action.CLICK_SUGGESTION_PRODUCT_LINE,
                        EVENT_LABEL, label,
                        ECOMMERCE, DataLayer.mapOf(
                                CLICK, DataLayer.mapOf(
                                        ACTION_FIELD, DataLayer.mapOf(LIST, AutocompleteEventTracking.Other.PRODUCT_LINE_SUGGESTION_ACTION_FIELD),
                                        PRODUCTS, DataLayer.listOf(
                                                productDataLayer
                                        )
                                )
                        ),
                        BUSINESS_UNIT, AutocompleteEventTracking.Iris.SEARCH,
                        CURRENT_SITE, AutocompleteEventTracking.Iris.TOKOPEDIA_MARKETPLACE,
                        USER_ID, userId
                )
        );
    }
}
