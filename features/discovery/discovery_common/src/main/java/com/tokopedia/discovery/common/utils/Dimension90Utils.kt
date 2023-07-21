package com.tokopedia.discovery.common.utils

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant.CustomDimension.DEFAULT_VALUE_CUSTOM_DIMENSION_90_GLOBAL
import com.tokopedia.discovery.common.constants.SearchConstant.CustomDimension.DEFAULT_VALUE_CUSTOM_DIMENSION_90_GLOBAL_SHOP
import com.tokopedia.discovery.common.constants.SearchConstant.CustomDimension.DIMENSION_90_GLOBAL_MPS

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
            else -> searchParameter.getDefaultDimension90(pageTitle, navSource, pageId)
        }
    }

    private fun Map<String, Any>.getDefaultDimension90(
        pageTitle: String,
        navSource: String,
        pageId: String,
    ): String {
        return when(get(SearchApiConst.ACTIVE_TAB)) {
             SearchApiConst.ACTIVE_TAB_MPS -> pageTitle.orNone() +
                ".${navSource.orNone()}" +
                ".$DIMENSION_90_GLOBAL_MPS" +
                ".${pageId.orNone()}"
             SearchApiConst.ACTIVE_TAB_SHOP -> DEFAULT_VALUE_CUSTOM_DIMENSION_90_GLOBAL_SHOP
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
