package com.tokopedia.filter.newdynamicfilter.analytics;

public interface FilterEventTracking {

    interface Event {
        String SEARCH_RESULT = "clickSearchResult";
    }

    interface Category {
        String FILTER_JOURNEY = "filter journey";
        String FILTER = "Filter";
        String FILTER_PRODUCT = "filter product";
        String PREFIX_SEARCH_RESULT_PAGE = "Search Result Page";
        String PREFIX_CATEGORY_PAGE = "Category Page";
    }

    interface Action {
        String CLICK = "Click";
        String SIMPAN_ON_LIHAT_SEMUA = "click simpan on lihat semua ";
        String BACK_ON_LIHAT_SEMUA = "click back on lihat semua ";
        String CLICK_LIHAT_SEMUA = "click lihat semua ";
        String CLICK_FILTER = "click filter";
        String FILTER = "Filter";
        String APPLY_FILTER = "apply filter";
    }
}
