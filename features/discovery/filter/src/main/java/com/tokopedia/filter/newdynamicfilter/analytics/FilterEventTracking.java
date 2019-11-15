package com.tokopedia.filter.newdynamicfilter.analytics;

public interface FilterEventTracking {

    interface Event {
        String CLICK_IMAGE_SEARCH_RESULT = "clickImageSearchResult";
        String CLICK_SEARCH_RESULT = "clickSearchResult";
        String CLICK_CATEGORY = "clickCategory";
        String CLICK_CATALOG_DETAIL = "clickCatalogDetail";
    }

    interface Category {
        String FILTER_JOURNEY = "filter journey";
        String FILTER = "Filter";
        String FILTER_PRODUCT = "filter product";
        String FILTER_CATALOG = "filter catalog";
        String FILTER_SHOP = "filter shop";
        String FILTER_CATEGORY = "filter category";
        String FILTER_CATALOG_DETAIL = "filter catalog detail";
        String PREFIX_SEARCH_RESULT_PAGE = "search result page";
        String PREFIX_IMAGE_SEARCH_RESULT_PAGE = "image search result page";
        String PREFIX_CATEGORY_PAGE = "category page";
        String PREFIX_CATALOG_PAGE = "catalog page";
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

    interface CustomDimension {
        String CATEGORY_ID = "categoryId";
    }
}
