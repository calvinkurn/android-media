package com.tokopedia.salam.umrah.search.util

import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant
/**
 * @author by M on 11/11/2019
 */
enum class SearchOrCategory {
    SEARCH {
        override fun getEventCategory(): String = UmrahTrackingConstant.SEARCH_RESULT_PAGE_UMROH
    },
    CATEGORY {
        override fun getEventCategory(): String = UmrahTrackingConstant.CATEGORY_RESULT_PAGE_UMROH
    };

    abstract fun getEventCategory(): String
}