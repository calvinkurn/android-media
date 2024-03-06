package com.tokopedia.discovery.common.analytics

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogParam.ACTIVITY_HASH_CODE
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

        val homeTabData = pageDataList
            .find { it[PAGE_NAME] == HOME }
            ?: return "" // App never goes to home, immediately return ""

        // MainParentActivity hash code
        val homePageHashCode = homeTabData[ACTIVITY_HASH_CODE]?.toString() ?: return ""

        // pageDataList for MainParentActivity
        val homePageTabDataList = pageDataList.filter { it[ACTIVITY_HASH_CODE]?.toString() == homePageHashCode }

        // PAGE_NAME of active tab in MainParentActivity
        val homePageActiveTabPageName = homePageTabDataList.lastOrNull()?.get(PAGE_NAME)?.toString().orEmpty()

        // Can only return "homepage" or ""
        return if (homePageActiveTabPageName != HOME) ""
        else homePageActiveTabPageName
    }
}
