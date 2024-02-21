package com.tokopedia.discovery.common.analytics

import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SEARCH_ID
import com.tokopedia.discovery.common.utils.SRP
import com.tokopedia.discovery.common.utils.SearchNavigationRecord

object SearchId {

    private val searchPageEntries
        get() = SearchNavigationRecord.pageList.filter { it.key.startsWith(SRP) }.entries.toList()

    private val currentSearchPageIndex
        get() = searchPageEntries.lastIndex

    private val currentSearchPageEntry
        get() = searchPageEntries.getOrNull(currentSearchPageIndex)

    private val previousSearchPageIndex
        get() = currentSearchPageIndex - 1

    private val previousSearchPageEntry
        get() = searchPageEntries.getOrNull(previousSearchPageIndex)

    val value: String
        get() = currentSearchPageEntry?.value?.get(SEARCH_ID) ?: ""

    val previousValue: String
        get() = previousSearchPageEntry?.value?.get(SEARCH_ID) ?: ""
}
