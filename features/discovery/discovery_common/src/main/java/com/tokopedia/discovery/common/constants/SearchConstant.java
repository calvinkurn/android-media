package com.tokopedia.discovery.common.constants;

public interface SearchConstant {

    String EXTRA_SEARCH_PARAMETER_MODEL = "EXTRA_SEARCH_PARAMETER_MODEL";

    String EXTRA_CATALOG_ID = "EXTRA_CATALOG_ID";

    String FROM_APP_SHORTCUTS = "FROM_APP_SHORTCUTS" ;

    String SEARCH_RESULT_PAGE = "search result page";
    String AB_TEST_REMOTE_CONFIG = "ab_test_remote_config";
    int CPM_TEMPLATE_ID = 4;
    int GENERAL_SEARCH_TRACKING_PRODUCT_COUNT = 3;

    interface Wishlist {
        String WISHLIST_STATUS_UPDATED_POSITION = "wishlistUpdatedPosition";
        String WISHLIST_STATUS_IS_WISHLIST = "isWishlist";
        String WISHLIST_PRODUCT_ID = "product_id";
        String PRODUCT_WISHLIST_URL = "product_wishlist_url";
        String PRODUCT_WISHLIST_URL_USE_CASE = "product_wishlist_url_use_case";
    }

    interface GQL {
        String KEY_QUERY = "query";
        String KEY_PARAMS = "params";
        String KEY_SOURCE = "source";
        String KEY_HEADLINE_PARAMS = "headline_params";
        String KEY_QUICK_FILTER_PARAMS = "quick_filter_params";
        String KEY_PAGE_SOURCE = "page_source";
        String PAGE_SOURCE_SEARCH_SHOP = "search_shop";
        String SOURCE_QUICK_FILTER = "quick_filter";
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
        String SEARCH_PRODUCT_TOPADS_USE_CASE = "search_product_topads_use_case";
        String GET_PRODUCT_COUNT_USE_CASE = "get_product_count_use_case";
        String GET_LOCAL_SEARCH_RECOMMENDATION_USE_CASE = "get_local_search_recommendation_use_case";
        String SEARCH_PRODUCT_GET_INSPIRATION_CAROUSEL_CHIPS_PRODUCTS_USE_CASE = "search_product_inspiration_carousel_chips_use_case";
        String SEARCH_SAME_SESSION_RECOMMENDATION_USE_CASE = "search_product_inspiration_carousel_chips_use_case";
        String SEARCH_PRODUCT_PARAMS = "params";
        String SEARCH_PRODUCT_SKIP_PRODUCT_ADS = "skip_product_ads";
        String SEARCH_PRODUCT_SKIP_HEADLINE_ADS = "skip_headline_ads";
        String SEARCH_PRODUCT_SKIP_GLOBAL_NAV = "skip_global_nav";
        String SEARCH_PRODUCT_SKIP_INSPIRATION_CAROUSEL = "skip_inspiration_carousel";
        String SEARCH_PRODUCT_SKIP_INSPIRATION_WIDGET = "skip_inspiration_widget";
        String SEARCH_PRODUCT_SKIP_GET_LAST_FILTER_WIDGET = "skip_last_filter";
    }

    interface HeadlineAds {
        String HEADLINE = "headline";
        String HEADLINE_TEMPLATE_VALUE = "3,4";
        int HEADLINE_ITEM_VALUE_FIRST_PAGE = 2;
        int HEADLINE_ITEM_VALUE_LOAD_MORE = 1;
        int HEADLINE_PRODUCT_COUNT = 3;
        String INFINITESEARCH = "infinitesearch";
    }

    interface SearchShop {
        String SEARCH_SHOP_FIRST_PAGE_USE_CASE = "search_shop_first_page_use_case";
        String SEARCH_SHOP_LOAD_MORE_USE_CASE = "search_shop_load_more_use_case";
        String GET_SHOP_COUNT_USE_CASE = "get_shop_count_use_case";
        String HEADLINE = "headline";
        String HEADLINE_TEMPLATE_VALUE = "3";
        String HEADLINE_ITEM_VALUE = "1";
        String ADS_SOURCE = "search";
        int SHOP_PRODUCT_PREVIEW_ITEM_MAX_COUNT = 3;
        int HEADLINE_PRODUCT_COUNT = 3;
    }

    interface MPS {
        String MPS_USE_CASE = "mps_use_case";
    }

    interface ShopStatus {
        int KEY_SHOP_IS_GOLD = 1;
        int KEY_SHOP_STATUS_CLOSED = 2;
        int KEY_SHOP_STATUS_MODERATED = 3;
        int KEY_SHOP_STATUS_INACTIVE = 4;
    }

    interface SearchTabPosition {
        int TAB_FIRST_POSITION = 0;
        int TAB_SECOND_POSITION = 1;
    }

    interface Cart {
        String CART_LOCAL_CACHE_NAME = "CART";
        String CACHE_TOTAL_CART = "CACHE_TOTAL_CART";
        String CART_LOCAL_CACHE = "CART_LOCAL_CACHE";
    }

    interface ActiveTab {
        String PRODUCT = "product";
        String SHOP = "shop";
        String MPS = "mps";
    }

    interface FreeOngkir {
        String FREE_ONGKIR_LOCAL_CACHE_NAME = "SEARCH_PRODUCT_FREE_ONGKIR";
        String FREE_ONGKIR_SHOW_CASE_ALREADY_SHOWN = "FREE_ONGKIR_SHOW_CASE_ALREADY_SHOWN";
    }

    interface SimilarSearch {
        String QUERY = "SIMILAR_SEARCH_QUERY";
    }

    interface ProductCardLabel {
        String LABEL_FULFILLMENT = "fulfillment";
        String LABEL_INTEGRITY = "integrity";
        String LABEL_INTEGRITY_TYPE = "textDarkGrey";
        String TEXT_DARK_ORANGE = "textDarkOrange";
        String TEXT_DARK_RED = "textDarkRed";
        String TEXT_LIGHT_GREY = "textLightGrey";
    }

    interface OnBoarding {
        String LOCAL_CACHE_NAME = "SEARCH_PRODUCT_ON_BOARDING";
        String FILTER_ONBOARDING_SHOWN = "FILTER_ONBOARDING_SHOWN";
    }

    interface InspirationCarousel {
        String TYPE_ANNOTATION_PRODUCT_COLOR_CHIPS = "annotation_product_color_chips";
        String TYPE_INSPIRATION_CAROUSEL_KEYWORD = "keyword";
        String TYPE_INSPIRATION_CAROUSEL_SINGLE_BUNDLING = "single_bundling";
        String TYPE_INSPIRATION_CAROUSEL_MULTIPLE_BUNDLING = "multiple_bundling";
        String TYPE_SAME_SESSION_RECOMMENDATION = "same_session";
    }

    interface TopAdsComponent {
        String TOP_ADS = "search_product_top_ads";
        String ORGANIC_ADS = "search_product_organic_ads";
        String BROAD_MATCH_ADS = "search_product_broad_match_ads";
    }

    interface InspirationCard {
        String TYPE_ANNOTATION = "annotation";
        String TYPE_CATEGORY = "category";
        String TYPE_GUIDED = "guided";
        String TYPE_CURATED = "curated";
        String TYPE_RELATED = "related";
        String TYPE_SIZE_PERSO = "size_perso";
    }

    interface CustomDimension {
        String DEFAULT_VALUE_CUSTOM_DIMENSION_90_GLOBAL = "none.none.global_search.none";
    }

    interface SaveLastFilter {
        String LAST_FILTER = "last_filter";
        String ACTION = "action";
        String PARAM = "param";
        String CATEGORY_ID_L2 = "category_id_l2";
        String ACTION_CREATE = "create";
        String ACTION_UPDATE = "update";
        String ACTION_DELETE = "delete";
        String INPUT_PARAMS = "input_params";
        String SAVE_LAST_FILTER_USE_CASE = "save_last_filter_use_case";
    }

    interface ProductListType {
        String VAR_LONG_IMG = "var_long_img";
        String VAR_REPOSITION = "var_reposition";
    }
}
