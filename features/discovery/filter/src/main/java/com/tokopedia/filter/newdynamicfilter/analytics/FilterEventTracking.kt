package com.tokopedia.filter.newdynamicfilter.analytics

interface FilterEventTracking {

    interface Event {
        companion object {
            const val CLICK_FILTER = "clickFilter"
            const val CLICK_SEARCH_RESULT = "clickSearchResult"
            const val CLICK_CATEGORY = "clickCategory"
            const val CLICK_CATALOG_DETAIL = "clickCatalogDetail"
            const val CLICK_IMAGE_SEARCH_RESULT = "clickImageSearchResult"
        }
    }

    interface Category {
        companion object {
            const val FILTER_JOURNEY = "filter journey"
            const val FILTER = "Filter"
            const val FILTER_PRODUCT = "filter product"
            const val FILTER_CATALOG = "filter catalog"
            const val FILTER_SHOP = "filter shop"
            const val FILTER_CATEGORY = "filter category"
            const val FILTER_CATALOG_DETAIL = "filter catalog detail"
            const val PREFIX_SEARCH_RESULT_PAGE = "search result page"
            const val PREFIX_CATEGORY_PAGE = "category page"
            const val PREFIX_CATALOG_PAGE = "catalog page"
            const val PREFIX_IMAGE_SEARCH_RESULT_PAGE = "image search result page"
        }
    }

    interface Action {
        companion object {
            const val CLICK = "Click"
            const val SIMPAN_ON_LIHAT_SEMUA = "click simpan on lihat semua "
            const val BACK_ON_LIHAT_SEMUA = "click back on lihat semua "
            const val CLICK_LIHAT_SEMUA = "click lihat semua "
            val CLICK_FILTER = "click filter"
            const val FILTER = "Filter"
            const val APPLY_FILTER = "apply filter"
        }
    }

    interface CustomDimension {
        companion object {
            const val CATEGORY_ID = "categoryId"
        }
    }
}
