package com.tokopedia.discovery.common.utils

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant.CustomDimension.DEFAULT_VALUE_CUSTOM_DIMENSION_90_GLOBAL

object Dimension90Utils {

    internal const val LOCAL_SEARCH = "local_search"
    internal const val NONE = "none"

    @JvmStatic
    fun getDimension90(searchParameter: Map<String, Any>): String {
        val navSource = searchParameter.getValueString(SearchApiConst.NAVSOURCE)
        val pageId = searchParameter.getValueString(SearchApiConst.SRP_PAGE_ID)
        val pageTitle = searchParameter.getValueString(SearchApiConst.SRP_PAGE_TITLE)
        val searchRef = searchParameter.getValueString(SearchApiConst.SEARCH_REF)

        val isLocalSearch = navSource.isNotEmpty() && pageId.isNotEmpty()
        val isTokonow = navSource.contains(SearchApiConst.DEFAULT_VALUE_OF_NAVSOURCE_TOKONOW)
        return when {
            isLocalSearch || isTokonow ->
                pageTitle.orNone() +
                    ".$navSource" +
                    ".$LOCAL_SEARCH" +
                    ".${pageId.orNone()}"
            searchRef.isNotEmpty() -> searchRef
            else -> DEFAULT_VALUE_CUSTOM_DIMENSION_90_GLOBAL
        }
    }

    private fun Map<String, Any>?.getValueString(key: String): String {
        this ?: return ""

        return get(key)?.toString() ?: ""
    }

    private fun String?.orNone(): String {
        return if (this == null || this.isEmpty()) NONE
        else this
    }
}