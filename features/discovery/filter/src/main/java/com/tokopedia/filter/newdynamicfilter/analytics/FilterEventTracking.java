package com.tokopedia.filter.newdynamicfilter.analytics;

public interface FilterEventTracking {

    interface Event {
        String SEARCH_RESULT = "clickSearchResult";
    }

    interface Category {
        String FILTER_JOURNEY = "filter journey";
        String FILTER = "Filter";
    }

    interface Action {
        String CLICK = "Click";
        String SIMPAN_ON_LIHAT_SEMUA = "click simpan on lihat semua ";
        String BACK_ON_LIHAT_SEMUA = "click back on lihat semua ";
        String CLICK_LIHAT_SEMUA = "click lihat semua ";
        String CLICK_FILTER = "click filter";
    }
}
