package com.tokopedia.salam.umrah.search.util

import com.tokopedia.salam.umrah.common.analytics.CATEGORY_RESULT_PAGE_UMROH
import com.tokopedia.salam.umrah.common.analytics.SEARCH_RESULT_PAGE_UMROH
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingAnalytics

/**
 * @author by M on 11/11/2019
 */
enum class SearchOrCategory {
    SEARCH {
        override fun getEventCategory(): String = SEARCH_RESULT_PAGE_UMROH
    },
    CATEGORY {
        override fun getEventCategory(): String = CATEGORY_RESULT_PAGE_UMROH
    };

    abstract fun getEventCategory(): String
}