package com.tokopedia.filter.newdynamicfilter.analytics

interface FilterEventTracking {

    interface Event {
        companion object {
            val CLICK_SEARCH_RESULT = "clickSearchResult"
            val CLICK_CATEGORY = "clickCategory"
            val CLICK_CATALOG_DETAIL = "clickCatalogDetail"
        }
    }

    interface Category {
        companion object {
            val FILTER_JOURNEY = "filter journey"
            val FILTER = "Filter"
            val FILTER_PRODUCT = "filter product"
            val FILTER_CATALOG = "filter catalog"
            val FILTER_SHOP = "filter shop"
            val FILTER_CATEGORY = "filter category"
            val FILTER_CATALOG_DETAIL = "filter catalog detail"
            val PREFIX_SEARCH_RESULT_PAGE = "search result page"
            val PREFIX_CATEGORY_PAGE = "category page"
            val PREFIX_CATALOG_PAGE = "catalog page"
        }
    }

    interface Action {
        companion object {
            val CLICK = "Click"
            val SIMPAN_ON_LIHAT_SEMUA = "click simpan on lihat semua "
            val BACK_ON_LIHAT_SEMUA = "click back on lihat semua "
            val CLICK_LIHAT_SEMUA = "click lihat semua "
            val CLICK_FILTER = "click filter"
            val FILTER = "Filter"
            val APPLY_FILTER = "apply filter"
        }
    }

    interface CustomDimension {
        companion object {
            val CATEGORY_ID = "categoryId"
        }
    }
}
