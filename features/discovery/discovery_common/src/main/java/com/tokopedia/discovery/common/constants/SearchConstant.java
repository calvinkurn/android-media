package com.tokopedia.discovery.common.constants;

public interface SearchConstant {

    String EXTRA_IS_AUTOCOMPLETE= "EXTRA_IS_AUTOCOMPLETE";
    String EXTRA_SEARCH_PARAMETER_MODEL = "EXTRA_SEARCH_PARAMETER_MODEL";

    String EXTRA_HAS_CATALOG = "EXTRA_HAS_CATALOG";
    String EXTRA_FORCE_SWIPE_TO_SHOP = "EXTRA_FORCE_SWIPE_TO_SHOP";

    String EXTRA_FORCE_SEARCH = "EXTRA_FORCE_SEARCH";
    String EXTRA_ACTIVE_TAB_POSITION = "EXTRA_ACTIVE_TAB_POSITION";

    String FROM_APP_SHORTCUTS = "FROM_APP_SHORTCUTS" ;
    String DEEP_LINK_URI = "deep_link_uri";

    String SEARCH_RESULT_TRACE = "search_result_trace";

    String GCM_ID = "gcm_id";
    String GCM_STORAGE = "GCM_STORAGE";

    String TRANSITION = "transition";

    int LANDSCAPE_COLUMN_MAIN = 3;
    int PORTRAIT_COLUMN_MAIN = 2;

    interface RecyclerView {
        int VIEW_PRODUCT = 3;
        int VIEW_PRODUCT_GRID_1 = 12;
        int VIEW_PRODUCT_GRID_2 = 13;
    }

    enum GridType {
        GRID_1, GRID_2, GRID_3
    }

    interface Wishlist {
        String WISHLIST_STATUS_UPDATED_POSITION = "wishlistUpdatedPosition";
        String WIHSLIST_STATUS_IS_WISHLIST = "isWishlist";
        String PRODUCT_WISHLIST_URL = "product_wishlist_url";
        String PRODUCT_WISHLIST_URL_USE_CASE = "product_wishlist_url_use_case";
    }

    interface GQL {
        String KEY_QUERY = "query";
        String KEY_PARAMS = "params";
        String KEY_SOURCE = "source";
        String KEY_HEADLINE_PARAMS = "headline_params";

        String GQL_INITIATE_SEARCH = "gql_initiate_search";
        String GQL_SEARCH_PRODUCT_FIRST_PAGE = "gql_search_product_first_page";
        String GQL_SEARCH_PRODUCT_LOAD_MORE = "gql_search_product_load_more";
    }

    interface SearchProduct {
        String SEARCH_PRODUCT_FIRST_PAGE_USE_CASE = "search_product_first_page_use_case";
        String SEARCH_PRODUCT_LOAD_MORE_USE_CASE = "search_product_load_more_use_case";
        String PARAMETER_ROWS = "8";
        String HEADLINE = "headline";
        String HEADLINE_TEMPLATE_VALUE = "3,4";
        String HEADLINE_ITEM_VALUE = "1";
    }

    interface RemoteConfigKey {
        String APP_CHANGE_PARAMETER_ROW = "mainapp_change_parameter_row";
    }

    interface BaseUrl {
        String TOPADS_DOMAIN = "https://ta.tokopedia.com/";
        String PATH_GET_DYNAMIC_ATTRIBUTE = "v2/dynamic_attributes";
        String PATH_GET_DYNAMIC_ATTRIBUTE_V4 = "v4/dynamic_attributes";
    }
}
