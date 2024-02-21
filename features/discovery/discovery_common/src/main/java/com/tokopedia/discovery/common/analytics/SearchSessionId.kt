package com.tokopedia.discovery.common.analytics

import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.EC_SEARCH_SESSION_ID
import com.tokopedia.discovery.common.utils.SRP
import com.tokopedia.discovery.common.utils.SearchNavigationRecord

object SearchSessionId {

    private fun firstSearchPageEntry(): Map.Entry<String, Map<String, String>>? =
        SearchNavigationRecord.pageList.filter {
            it.key.startsWith(SRP, ignoreCase = true)
                && it.value.containsKey(EC_SEARCH_SESSION_ID)
        }.entries.firstOrNull()

    private fun firstSearchPageData() = firstSearchPageEntry()?.value ?: mapOf()

    val value: String
        get() = firstSearchPageData()[EC_SEARCH_SESSION_ID] ?: ""
}
