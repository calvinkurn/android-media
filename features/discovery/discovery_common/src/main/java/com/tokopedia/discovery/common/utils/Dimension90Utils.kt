package com.tokopedia.discovery.common.utils

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant

object Dimension90Utils {

    @JvmStatic
    fun getDimension90(searchParameter: Map<String, Any>): String {
        val navSource = searchParameter.getValueString(SearchApiConst.NAVSOURCE)
        val pageId = searchParameter.getValueString(SearchApiConst.SRP_PAGE_ID)
        val pageTitle = searchParameter.getValueString(SearchApiConst.SRP_PAGE_TITLE)
        val searchRef = searchParameter.getValueString(SearchApiConst.SEARCH_REF)

        return if (navSource.isNotEmpty() && pageId.isNotEmpty()) "$pageTitle.$navSource.local_search.$pageId"
        else if (searchRef.isNotEmpty()) searchRef
        else SearchConstant.CustomDimension.DEFAULT_VALUE_CUSTOM_DIMENSION_90_GLOBAL
    }

    private fun Map<String, Any>?.getValueString(key: String): String {
        this ?: return ""

        return get(key)?.toString() ?: ""
    }
}