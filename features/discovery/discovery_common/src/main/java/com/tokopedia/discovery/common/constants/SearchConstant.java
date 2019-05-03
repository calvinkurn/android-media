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

    String GQL_INITIATE_SEARCH = "gql_initiate_search";

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
}
