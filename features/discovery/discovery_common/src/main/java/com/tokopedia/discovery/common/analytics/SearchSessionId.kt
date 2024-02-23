package com.tokopedia.discovery.common.analytics

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.EC_SEARCH_SESSION_ID

object SearchSessionId {

    val value: String
        get() = (AppLogAnalytics.getLastData(EC_SEARCH_SESSION_ID) ?: "").toString()

    fun update() {
        if (value.isBlank())
            AppLogAnalytics.putPageData(EC_SEARCH_SESSION_ID, System.currentTimeMillis())
    }
}
