package com.tokopedia.discovery.common.constants;

public interface SearchConstant {

    String EXTRA_IS_AUTOCOMPLETE= "EXTRA_IS_AUTOCOMPLETE";
    String EXTRA_SEARCH_PARAMETER_MODEL = "EXTRA_SEARCH_PARAMETER_MODEL";

    String EXTRA_HAS_CATALOG = "EXTRA_HAS_CATALOG";
    String EXTRA_FORCE_SWIPE_TO_SHOP = "EXTRA_FORCE_SWIPE_TO_SHOP";

    String EXTRA_ACTIVE_TAB_POSITION = "EXTRA_ACTIVE_TAB_POSITION";

    String EXTRA_CATALOG_ID = "EXTRA_CATALOG_ID";

    String FROM_APP_SHORTCUTS = "FROM_APP_SHORTCUTS" ;
    String DEEP_LINK_URI = "deep_link_uri";

    String SEARCH_RESULT_TRACE = "search_result_trace";

    String GCM_ID = "gcm_id";
    String GCM_STORAGE = "GCM_STORAGE";

    String TRANSITION = "transition";

    int LANDSCAPE_COLUMN_MAIN = 3;
    int PORTRAIT_COLUMN_MAIN = 2;

    int AUTO_COMPLETE_ACTIVITY_REQUEST_CODE = 14332;
    int AUTO_COMPLETE_ACTIVITY_RESULT_CODE_START_ACTIVITY = 12323;

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
        String ACE_DOMAIN = "https://ace.tokopedia.com/";
        String TOME_DOMAIN = "https://tome.tokopedia.com/";
    }

    interface Ace {
        String PATH_GET_DYNAMIC_ATTRIBUTE = "v2/dynamic_attributes";
        String PATH_GET_DYNAMIC_ATTRIBUTE_V4 = "v4/dynamic_attributes";
        String PATH_BROWSE_SHOP = "search/v1/shop";
        String PATH_BROWSE_CATALOG = "search/v2.1/catalog";
    }

    interface Tome {
        String PATH_IS_FAVORITE_SHOP = "v1/user/isfollowing";
    }

    interface DynamicFilter {
        String GET_DYNAMIC_FILTER_USE_CASE = "get_dynamic_filter_use_case";
        String GET_DYNAMIC_FILTER_V4_USE_CASE = "get_dynamic_filter_v4_use_case";
        String GET_DYNAMIC_FILTER_GQL_USE_CASE = "get_dynamic_filter_gql_use_case";
        String DYNAMIC_FILTER_REPOSITORY = "dynamic_filter_repository";
        String DYNAMIC_FILTER_REPOSITORY_V4 = "dynamic_filter_repository_v4";
    }

    interface SearchShop {
        String SEARCH_SHOP_USE_CASE = "search_shop_use_case";
        String TOGGLE_FAVORITE_SHOP_USE_CASE = "toggle_favorite_shop_use_case";
        String TOGGLE_FAVORITE_SHOP_ID = "SHOP_ID";
    }

    interface SearchCatalog {
        String SEARCH_CATALOG_USE_CASE = "search_catalog_use_case";
    }

    interface SearchProfile {
        String SEARCH_PROFILE_USE_CASE = "search_profile_use_case";
    }
}
