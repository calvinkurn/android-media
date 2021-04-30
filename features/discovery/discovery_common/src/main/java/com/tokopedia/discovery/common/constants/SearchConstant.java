package com.tokopedia.discovery.common.constants;

public interface SearchConstant {

    String EXTRA_SEARCH_PARAMETER_MODEL = "EXTRA_SEARCH_PARAMETER_MODEL";

    String EXTRA_CATALOG_ID = "EXTRA_CATALOG_ID";

    String FROM_APP_SHORTCUTS = "FROM_APP_SHORTCUTS" ;

    String SEARCH_RESULT_PAGE = "search result page";
    String SEARCH_RESULT_TRACE = "search_result_trace";
    String SEARCH_RESULT_PLT_PREPARE_METRICS = "search_result_plt_prepare_metrics";
    String SEARCH_RESULT_PLT_NETWORK_METRICS = "search_result_plt_network_metrics";
    String SEARCH_RESULT_PLT_RENDER_METRICS = "search_result_plt_render_metrics";

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
        String VIEW_TYPE_NAME_SMALL_GRID = "grid 2";
        String VIEW_TYPE_NAME_BIG_GRID = "grid 1";
        String VIEW_TYPE_NAME_LIST = "list";
    }

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
        String GET_PRODUCT_COUNT_USE_CASE = "get_product_count_use_case";
        String GET_LOCAL_SEARCH_RECOMMENDATION_USE_CASE = "get_local_search_recommendation_use_case";
        String SEARCH_PRODUCT_GET_INSPIRATION_CAROUSEL_CHIPS_PRODUCTS_USE_CASE = "search_product_inspiration_carousel_chips_use_case";
        String HEADLINE = "headline";
        String HEADLINE_TEMPLATE_VALUE = "3,4";
        String HEADLINE_ITEM_VALUE = "1";
        String SEARCH_PRODUCT_PARAMS = "params";
        String SEARCH_PRODUCT_SKIP_PRODUCT_ADS = "skip_product_ads";
        String SEARCH_PRODUCT_SKIP_HEADLINE_ADS = "skip_headline_ads";
        String SEARCH_PRODUCT_SKIP_GLOBAL_NAV = "skip_global_nav";
        String SEARCH_PRODUCT_SKIP_INSPIRATION_CAROUSEL = "skip_inspiration_carousel";
        String SEARCH_PRODUCT_SKIP_INSPIRATION_WIDGET = "skip_inspiration_widget";
    }

    interface SearchShop {
        String SEARCH_SHOP_FIRST_PAGE_USE_CASE = "search_shop_first_page_use_case";
        String SEARCH_SHOP_LOAD_MORE_USE_CASE = "search_shop_load_more_use_case";
        String GET_SHOP_COUNT_USE_CASE = "get_shop_count_use_case";
        String HEADLINE = "headline";
        String HEADLINE_TEMPLATE_VALUE = "3";
        String HEADLINE_ITEM_VALUE = "1";
        String ADS_SOURCE = "search";
        String SEARCH_SHOP_VIEW_MODEL_FACTORY = "search_shop_view_model_factory";
        int SHOP_PRODUCT_PREVIEW_ITEM_MAX_COUNT = 3;
        int HEADLINE_PRODUCT_COUNT = 3;
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
    }

    interface FreeOngkir {
        String FREE_ONGKIR_LOCAL_CACHE_NAME = "SEARCH_PRODUCT_FREE_ONGKIR";
        String FREE_ONGKIR_SHOW_CASE_ALREADY_SHOWN = "FREE_ONGKIR_SHOW_CASE_ALREADY_SHOWN";
    }

    interface SimilarSearch {
        String QUERY = "SIMILAR_SEARCH_QUERY";
    }

    interface ABTestRemoteConfigKey {
        String AB_TEST_KEY_COMMA_VS_FULL_STAR = "Comma vs Full Star";
        String AB_TEST_VARIANT_FULL_STAR = "Full Star";
        String AB_TEST_VARIANT_COMMA_STAR = "Comma Star";
        String AB_TEST_SHOP_RATING = "Hierarchical Rating Toko";
        String AB_TEST_SHOP_RATING_VARIANT_A = "Hierarchical Rating";
        String AB_TEST_SHOP_RATING_VARIANT_B = "Terjual ft Rating";
        String AB_TEST_SHOP_RATING_VARIANT_C = "Rating Only";
        String AB_TEST_KEY_THREE_DOTS_SEARCH = "3 Dots Search";
        String AB_TEST_THREE_DOTS_SEARCH_FULL_OPTIONS = "Full Options";
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
        String LAYOUT_INSPIRATION_CAROUSEL_LIST = "list";
        String LAYOUT_INSPIRATION_CAROUSEL_INFO = "info";
        String LAYOUT_INSPIRATION_CAROUSEL_GRID = "grid";
        String LAYOUT_INSPIRATION_CAROUSEL_CHIPS = "chips";
        String LAYOUT_INSPIRATION_CAROUSEL_DYNAMIC_PRODUCT = "dynamic_product";
        String LAYOUT_INSPIRATION_CAROUSEL_GRID_BANNER = "gridBanner";
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
    }
}
