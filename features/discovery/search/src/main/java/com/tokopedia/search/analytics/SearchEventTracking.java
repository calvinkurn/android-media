package com.tokopedia.search.analytics;

public interface SearchEventTracking {

    interface Event {
        String EVENT_VIEW_SEARCH_RESULT = "viewSearchResult";
        String CLICK_SEARCH = "clickSearch";
        String SEARCH_RESULT = "clickSearchResult";
        String CLICK_WISHLIST = "clickWishlist";
        String CLICK_TOP_NAV = "clickTopNav";
    }

    interface Category {
        String EVENT_TOP_NAV = "top nav";
        String EVENT_TOP_NAV_SEARCH_SRP = "top nav - search - search result page";
        String SEARCH = "Search";
        String FILTER_PRODUCT = "filter product";
        String SEARCH_RESULT = "search result";
        String GRID_MENU = "grid menu";
        String SEARCH_TAB = "search tab";
        String SORT = "Sort";
        String SORT_BY = "sort by";
        String TOP_NAV_SEARCH_RESULT_PAGE = "top nav - search result page";
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
        String CLICK_SEARCH = "click - search";
    }

    interface Label {
        String KEYWORD_PRODUCT_ID = "Keyword: %s - product id: %s";
        String TOPADS = "topads";
        String GENERAL = "general";
        String KEYWORD = "keyword: %s";
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
}
