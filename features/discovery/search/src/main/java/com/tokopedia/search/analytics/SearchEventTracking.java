package com.tokopedia.search.analytics;

public interface SearchEventTracking {

    interface Event {
        String EVENT_VIEW_SEARCH_RESULT = "viewSearchResult";
        String SEARCH = "clickSearch";
        String SEARCH_RESULT = "clickSearchResult";
        String CLICK_WISHLIST = "clickWishlist";
    }

    interface Category {
        String EVENT_TOP_NAV = "top nav";
        String SEARCH = "Search";
        String FILTER_PRODUCT = "filter product";
        String SEARCH_RESULT = "Search Result";
        String GRID_MENU = "grid menu";
        String SEARCH_TAB = "search tab";
        String SORT = "Sort";
        String SORT_BY = "sort by";
        String EventSearchResult = "search result";
    }

    interface Action {
        String QUICK_FILTER = "quick filter";
        String CLICK_CHANGE_GRID = "click - ";
        String CLICK_CATALOG = "click - catalog";
        String CLICK_TAB = "click - tab";
        String NO_SEARCH_RESULT_WITH_TAB = "no search result - tab: %s";
        String SORT_BY = "sort by";
        String EventLongPressProduct = "click - long press product";
        String ADD_WISHLIST = "add wishlist";
        String REMOVE_WISHLIST = "remove wishlist";
        String CLICK_CART_BUTTON = "click cart button - search result";
    }

    interface Label {
        String LabelKeywordProduct = "Keyword: %s - product id: %s";
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
