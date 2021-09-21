package com.tokopedia.catalog.model.util

class CatalogSearchApiConst {

    companion object {
        const val SOURCE = "source" // Source
        const val OB = "ob" // order by value, could be found at wiki
        const val PMIN = "pmin"
        const val PMAX = "pmax"
        const val Q = "q" // (keyword) or (query)
        const val HINT = "hint"
        const val ACTIVE_TAB = "st"
        const val ORIGIN_FILTER = "origin_filter" // which page filter params come from
        const val SEARCH_REF = "search_ref"
        const val PREVIOUS_KEYWORD = "previous_keyword"
        const val LANDING_PAGE = "landing_page"
        const val RF = "rf"
        const val SKIP_REWRITE = "skip_rewrite"
        const val NAVSOURCE = "navsource"
        const val SKIP_BROADMATCH = "skip_broadmatch"
        const val FIRST_INSTALL = "first_install"
        const val DEFAULT_VALUE_OF_PARAMETER_SORT = "23"
    }
}