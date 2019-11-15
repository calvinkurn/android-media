package com.tokopedia.discovery.common.constants;

public interface SearchConstant {

    String EXTRA_SEARCH_PARAMETER_MODEL = "EXTRA_SEARCH_PARAMETER_MODEL";

    String EXTRA_CATALOG_ID = "EXTRA_CATALOG_ID";

    String FROM_APP_SHORTCUTS = "FROM_APP_SHORTCUTS" ;

    String SEARCH_RESULT_TRACE = "search_result_trace";

    String SEARCH_VIEW_MODEL_FACTORY = "search_view_model_factory";

    int LANDSCAPE_COLUMN_MAIN = 3;
    int PORTRAIT_COLUMN_MAIN = 2;

    interface RecyclerView {
        int VIEW_LIST = 3;
        int VIEW_PRODUCT_BIG_GRID = 12;
        int VIEW_PRODUCT_SMALL_GRID = 13;
    }

    enum ViewType {
        LIST, SMALL_GRID, BIG_GRID
    }

    interface DefaultViewType {
        int SMALL_GRID = 1;
        int LIST = 2;
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
        String PATH_BROWSE_CATALOG = "search/v2.1/catalog";
    }

    interface DynamicFilter {
        String GET_DYNAMIC_FILTER_USE_CASE = "get_dynamic_filter_use_case";
        String GET_DYNAMIC_FILTER_V4_USE_CASE = "get_dynamic_filter_v4_use_case";
        String GET_DYNAMIC_FILTER_GQL_USE_CASE = "get_dynamic_filter_gql_use_case";
        String GET_DYNAMIC_FILTER_SHOP_USE_CASE = "get_dynamic_filter_shop_use_case";
        String DYNAMIC_FILTER_REPOSITORY = "dynamic_filter_repository";
        String DYNAMIC_FILTER_REPOSITORY_V4 = "dynamic_filter_repository_v4";
    }

    interface SearchProduct {
        String SEARCH_PRODUCT_FIRST_PAGE_USE_CASE = "search_product_first_page_use_case";
        String SEARCH_PRODUCT_LOAD_MORE_USE_CASE = "search_product_load_more_use_case";
        String PARAMETER_ROWS = "8";
        String HEADLINE = "headline";
        String HEADLINE_TEMPLATE_VALUE = "3,4";
        String HEADLINE_ITEM_VALUE = "1";
    }

    interface SearchShop {
        String SEARCH_SHOP_FIRST_PAGE_USE_CASE = "search_shop_first_page_use_case";
        String SEARCH_SHOP_LOAD_MORE_USE_CASE = "search_shop_load_more_use_case";
        String HEADLINE = "headline";
        String HEADLINE_TEMPLATE_VALUE = "3";
        String HEADLINE_ITEM_VALUE = "1";
        String ADS_SOURCE = "search";
        String SEARCH_SHOP_VIEW_MODEL_FACTORY = "search_shop_view_model_factory";
        int SHOP_PRODUCT_PREVIEW_ITEM_MAX_COUNT = 3;
    }

    interface ShopStatus {
        int KEY_SHOP_IS_GOLD = 1;
        int KEY_SHOP_STATUS_CLOSED = 2;
        int KEY_SHOP_STATUS_MODERATED = 3;
        int KEY_SHOP_STATUS_INACTIVE = 4;
    }

    interface SearchCatalog {
        String SEARCH_CATALOG_USE_CASE = "search_catalog_use_case";
    }

    interface SearchProfile {
        String SEARCH_PROFILE_USE_CASE = "search_profile_use_case";
    }

    interface SearchTabPosition {
        int TAB_FIRST_POSITION = 0;
        int TAB_SECOND_POSITION = 1;
        int TAB_THIRD_POSITION = 2;
        int TAB_FORTH_POSITION = 3;
    }

    interface Cart {
        String CART_LOCAL_CACHE_NAME = "CART";
        String CACHE_TOTAL_CART = "CACHE_TOTAL_CART";
        String CART_LOCAL_CACHE = "CART_LOCAL_CACHE";
    }

    interface GCM {
        String GCM_ID = "gcm_id";
        String GCM_STORAGE = "GCM_STORAGE";
        String GCM_LOCAL_CACHE = "GCM_LOCAL_CACHE";
    }

    interface ActiveTab {
        String PRODUCT = "product";
        String SHOP = "shop";
        String CATALOG = "catalog";
        String PROFILE = "profile";
    }

    interface FreeOngkir {
        String FREE_ONGKIR_LOCAL_CACHE_NAME = "SEARCH_PRODUCT_FREE_ONGKIR";
        String FREE_ONGKIR_SHOW_CASE_ALREADY_SHOWN = "FREE_ONGKIR_SHOW_CASE_ALREADY_SHOWN";
    }

    interface SimilarSearch {
        String QUERY = "SIMILAR_SEARCH_QUERY";
    }
}
