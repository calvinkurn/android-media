package com.tokopedia.search.analytics;

public interface SearchEventTracking {

    interface Event {
        String EVENT_VIEW_SEARCH_RESULT = "viewSearchResult";
        String CLICK_SEARCH = "clickSearch";
        String SEARCH_RESULT = "clickSearchResult";
        String CLICK_WISHLIST = "clickWishlist";
        String CLICK_TOP_NAV = "clickTopNav";
        String VIEW_SEARCH_RESULT_IRIS = "viewSearchResultIris";
        String CLICK_SEARCH_RESULT_IRIS = "clickSearchResultIris";
        String PROMO_VIEW = "promoView";
        String PROMO_CLICK = "promoClick";
        String PRODUCT_VIEW = "productView";
        String PRODUCT_CLICK = "productClick";
    }

    interface Category {
        String EVENT_TOP_NAV = "top nav";
        String EVENT_TOP_NAV_SEARCH_SRP = "top nav - search result page";
        String SEARCH = "Search";
        String FILTER_PRODUCT = "filter product";
        String SEARCH_RESULT = "search result";
        String GRID_MENU = "grid menu";
        String SEARCH_TAB = "search tab";
        String SORT = "Sort";
        String SORT_BY = "sort by";
        String TOP_NAV_SEARCH_RESULT_PAGE = "top nav - search result page";
        String SEARCH_RESULT_PROFILE = "search result profile";
    }

    interface Action {
        String QUICK_FILTER = "quick filter";
        String CLICK_CHANGE_GRID = "click - ";
        String CLICK_CATALOG = "click - catalog";
        String CLICK_TAB = "click - tab";
        String NO_SEARCH_RESULT_WITH_TAB = "no search result - tab: %s";
        String SORT_BY = "sort by";
        String LONG_PRESS_PRODUCT = "click - long press product";
        String ADD_WISHLIST = "add wishlist";
        String REMOVE_WISHLIST = "remove wishlist";
        String CLICK_CART_BUTTON_SEARCH_RESULT = "click cart button - search result";
        String CLICK_HOME_BUTTON_SEARCH_RESULT = "click home button - search result";
        String CLICK_SEARCH_BOX = "click search box";
        String MODULE = "module";
        String LOGIN = "login";
        String NON_LOGIN = "nonlogin";
        String IMPRESSION_BANNED_PRODUCT_TICKER_EMPTY = "impression - banned product ticker - empty";
        String CLICK_BANNED_PRODUCT_TICKER_EMPTY = "click - banned product ticker - empty";
        String IMPRESSION_BANNED_PRODUCT_TICKER_RELATED = "impression - banned product ticker - related";
        String IMPRESSION_TICKER = "impression - ticker";
        String CLICK_TICKER = "click - ticker";
        String CLICK_BANNED_PRODUCT_TICKER_RELATED = "click - banned product ticker - related";
        String GENERAL_SEARCH = "general search";
        String CLICK_CHANGE_KEYWORD = "click ganti kata kunci";
        String IMPRESSION_SHOP = "impression - shop";
        String IMPRESSION_SHOP_ALTERNATIVE = "impression - shop - alternative";
        String CLICK_SHOP = "click - shop";
        String CLICK_SHOP_INACTIVE = "click - shop - inactive";
        String CLICK_SHOP_ALTERNATIVE = "click - shop - alternative";
        String IMPRESSION_PRODUCT_SHOP_TAB = "impression - product - shop tab";
        String IMPRESSION_PRODUCT_SHOP_TAB_ALTERNATIVE = "impression - product - shop tab - alternative";
        String CLICK_PRODUCT_SHOP_TAB = "click - product - shop tab";
        String CLICK_PRODUCT_SHOP_TAB_ALTERNATIVE = "click - product - shop tab - alternative";
        String IMPRESSION_PROFILE = "impression - profile";
        String IMPRESSION_TOP_PROFILE_IN_NO_RESULT_PROFILE = "impression - top profile in no result profile";
        String CLICK_PROFILE_RESULT = "click - profile result";
        String CLICK_TOP_PROFILE_IN_NO_RESULT_PROFILE = "click - top profile in no result profile";
        String IMPRESSION_INSPIRATION_CAROUSEL_PRODUCT = "impression - inspiration carousel product";
        String CLICK_INSPIRATION_CAROUSEL_SEARCH = "click - inspiration carousel search";
        String CLICK_INSPIRATION_CAROUSEL_PRODUCT = "click - inspiration carousel product";
        String CLICK = "click";
        String CLICK_FUZZY_KEYWORDS_SUGGESTION = "click - fuzzy keywords - suggestion";
        String IMPRESSION_BROAD_MATCH = "impression - broad match";
        String CLICK_BROAD_MATCH_LIHAT_SEMUA = "click - broad match lihat semua";
        String CLICK_BROAD_MATCH = "click - broad match";
        String CLICK_INSPIRATION_CARD = "click inspiration card";
    }

    interface Label {
        String KEYWORD_PRODUCT_ID = "Keyword: %s - product id: %s";
        String TOPADS = "topads";
        String GENERAL = "general";
        String KEYWORD_TREATMENT_RESPONSE = "keyword: %s - treatment: %s - response: %s";
    }

    interface MOENGAGE {
        String KEYWORD = "keyword";
        String IS_RESULT_FOUND = "is_result_found";
        String CATEGORY_ID_MAPPING = "category_id_mapping";
        String CATEGORY_NAME_MAPPING = "category_name_mapping";
    }

    interface EventMoEngage {
        String SEARCH_ATTEMPT = "Search_Attempt";
    }

    String NONE = "none";
}
