package com.tokopedia.autocomplete.analytics;

public interface AutocompleteEventTracking {

    interface Event {
        String LONG_CLICK = "longClick";
        String SEARCH = "clickSearch";
        String CLICK_TOP_NAV = "clickTopNav";
        String CLICK_SEARCH = "clickSearch";
        String CLICK_SEARCH_RESULT = "clickSearchResult";
        String PRODUCT_VIEW_IRIS = "productViewIris";
        String PROMO_VIEW_IRIS = "promoViewIris";
        String VIEW_TOP_NAV_IRIS = "viewTopNavIris";
        String PRODUCT_CLICK = "productClick";
    }

    interface Category {
        String LONG_PRESS = "Long Press";
        String TOP_NAV = "top nav";
        String INITIAL_STATE = "initial-state";
        String SEARCH = "Search";
        String TOP_NAV_INITIAL_STATE = "top nav - initial state";
    }

    interface Action {
        String VOICE_SEARCH = "Voice Search";
        String CLICK_CARI = "Click Cari";
        String CLICK_SEE_MORE_RECENT_SEARCH = "click see more - recent search";
        String CLICK_POPULAR_SEARCH = "click - popular search";
        String CLICK_RECENT_SEARCH = "click - recent search";
        String CLICK_DIGITAL_PRODUCT_SUGGESTION = "click - digital product suggestion";
        String CLICK_SEARCH = "click - search";
        String CLICK_PROFILE_SUGGESTION = "click - profile autocomplete on suggestion list";
        String CLICK_REFRESH_POPULAR_SEARCH = "click refresh on popular search";
        String CLICK_SHOP_SUGGESTION = "click - shop autocomplete";
        String CLICK_KEYWORD_SUGGESTION = "click - product autocomplete";
        String CLICK_RECENT_SEARCH_AUTOCOMPLETE = "click - recent search autocomplete";
        String CLICK_RECENT_SHOP = "click - shop - recent search";
        String CLICK_TOP_SHOP = "click - shop - carousel";
        String CLICK_TOP_SHOP_SEE_MORE = "click - lihat toko lainnya - carousel";
        String CLICK_RECENT_VIEW_PRODUCT = "click - recent view product";
        String CLICK_DYNAMIC_SECTION = "click - popular search";
        String CLICK_LOCAL_KEYWORD = "click - autocomplete local";
        String CLICK_GLOBAL_KEYWORD = "click - autocomplete global";
        String IMPRESSION_RECENT_VIEW = "impression - recent view product";
        String IMPRESSION_RECENT_SEARCH = "impression - recent search";
        String IMPRESSION_POPULAR_SEARCH = "impression - popular search";
        String IMPRESSION_SEE_MORE_RECENT_SEARCH = "impression see more - recent search";
        String CLICK_CURATED_CAMPAIGN = "click - curated campaign";
        String IMPRESSION_CURATED_CAMPAIGN = "impression - curated campaign";
    }

    interface Label {
        String PRODUCT_SEARCH = "Product Search";
        String LABEL_RECENT_VIEW_CLICK = "po: %s - applink: %s";
    }

    interface Iris {
        String TOKOPEDIA_MARKETPLACE = "tokopediamarketplace";
        String SEARCH = "search";
    }

    interface Other {
        String NONE_OTHER = "none / other";
        String RECENT_VIEW_ACTION_FIELD = "/search - recentview - product";
    }

}
