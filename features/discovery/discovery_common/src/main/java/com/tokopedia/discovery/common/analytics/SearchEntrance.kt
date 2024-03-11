package com.tokopedia.discovery.common.analytics

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogParam.ENTER_FROM
import com.tokopedia.analytics.byteio.PageName.SEARCH_RESULT
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.GOODS_SEARCH
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.STORE_SEARCH

object SearchEntrance {

    private val blacklistedValue = listOf(SEARCH_RESULT, GOODS_SEARCH, STORE_SEARCH)

    fun value(): String =
        AppLogAnalytics.pageDataList
            .lastOrNull(::notBlacklistedEnterFrom)
            ?.get(ENTER_FROM)
            ?.toString()
            .orEmpty()

    private fun notBlacklistedEnterFrom(pageData: Map<String, Any>): Boolean {
        val enterFrom = pageData[ENTER_FROM]?.toString().orEmpty()
        return enterFrom.isNotBlank() && !blacklistedValue.contains(enterFrom)
    }
}
