package com.tokopedia.discovery.common.analytics

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SEARCH_ID

object SearchId {

    var value: String = ""
        private set

    var previousValue: String = ""
        private set

    fun update(searchId: String) {
        this.previousValue = this.value
        this.value = searchId

        AppLogAnalytics.putPageData(SEARCH_ID, searchId)
    }
}
