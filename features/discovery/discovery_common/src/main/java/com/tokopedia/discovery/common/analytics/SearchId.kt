package com.tokopedia.discovery.common.analytics

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.PRE_SEARCH_ID
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SEARCH_ID

object SearchId {

    val value: String
        get() = (AppLogAnalytics.getCurrentData(SEARCH_ID) ?: "").toString()

    private val currentPreSearchId
        get() = (AppLogAnalytics.getCurrentData(PRE_SEARCH_ID) ?: "").toString()

    val previousValue: String
        get() = currentPreSearchId.ifBlank {
            (AppLogAnalytics.getLastDataBeforeCurrent(SEARCH_ID) ?: "").toString()
        }

    fun update(value: String) {
        AppLogAnalytics.putPageData(PRE_SEARCH_ID, this.value)
        AppLogAnalytics.putPageData(SEARCH_ID, value)
    }
}
