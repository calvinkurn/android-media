package com.tokopedia.discovery.common.analytics

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogParam.IS_MAIN_PARENT
import com.tokopedia.analytics.byteio.AppLogParam.PAGE_NAME
import com.tokopedia.analytics.byteio.PageName.HOME

class SearchEntrance {

    val value: String = searchEntrance()

    /**
     * SEARCH_ENTRANCE is the PAGE_NAME of active tab in Home Page (MainParentActivity).
     * If the active tab is not "homepage", SEARCH_ENTRANCE will be empty.
     * Value is only calculated once for every Auto Complete and Search Page instances.
     */
    private fun searchEntrance(): String {
        val pageDataList = AppLogAnalytics.pageDataList

        // pageDataList for MainParentActivity
        val mainParentPageDataList = pageDataList.filter { it[IS_MAIN_PARENT]?.toString().toBoolean() }

        // PAGE_NAME of active tab in MainParentActivity
        val homePageActiveTabPageName = mainParentPageDataList.lastOrNull()?.get(PAGE_NAME)?.toString().orEmpty()

        // Can only return "homepage" or ""
        return if (homePageActiveTabPageName != HOME) ""
        else homePageActiveTabPageName
    }
}
