package com.tokopedia.createpost.common.util

interface CreatePostSearchConstant {

    interface SearchShop {
        companion object {
            const val SEARCH_SHOP_FIRST_PAGE_USE_CASE = "search_shop_first_page_use_case"
            const val SEARCH_SHOP_LOAD_MORE_USE_CASE = "search_shop_load_more_use_case"
            const val GET_SHOP_COUNT_USE_CASE = "get_shop_count_use_case"
            const val HEADLINE = "headline"
            const val HEADLINE_TEMPLATE_VALUE = "3"
            const val HEADLINE_ITEM_VALUE = "1"
            const val ADS_SOURCE = "search"
            const val SEARCH_SHOP_VIEW_MODEL_FACTORY = "search_shop_view_model_factory"
            const val SHOP_PRODUCT_PREVIEW_ITEM_MAX_COUNT = 3
            const val HEADLINE_PRODUCT_COUNT = 3
        }
    }
    interface GQL {
        companion object {
            const val KEY_QUERY = "query"
            const val KEY_PARAMS = "params"
            const val KEY_SOURCE = "source"
            const val KEY_HEADLINE_PARAMS = "headline_params"
            const val KEY_QUICK_FILTER_PARAMS = "quick_filter_params"
            const val KEY_PAGE_SOURCE = "page_source"
            const val PAGE_SOURCE_SEARCH_SHOP = "search_shop"
            const val SOURCE_QUICK_FILTER = "quick_filter"
        }
    }


}