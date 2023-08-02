package com.tokopedia.autocompletecomponent.util

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.autocompletecomponent.searchbar.SearchBarKeyword
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.discovery.common.utils.URLParser

internal fun getModifiedApplink(applink: String?, searchParameter: SearchParameter?): String {
    applink ?: return ""

    return if (applink.startsWith(ApplinkConst.DISCOVERY_SEARCH))
        getModifiedSearchResultApplink(applink, searchParameter)
    else applink
}

private fun getModifiedSearchResultApplink(applink: String?, searchParameter: SearchParameter?): String {
    applink ?: return ""

    val applinkQueryParams = URLParser(applink).paramKeyValueMap

    applinkQueryParams[SearchApiConst.PREVIOUS_KEYWORD] = searchParameter?.get(SearchApiConst.PREVIOUS_KEYWORD) ?: ""

    return ApplinkConst.DISCOVERY_SEARCH + "?" + UrlParamHelper.generateUrlParamString(applinkQueryParams)
}

internal fun getModifiedApplink(
    applink: String?,
    searchParameter: Map<String, String>?,
    activeKeyword: SearchBarKeyword? = null,
): String {
    applink ?: return ""

    return if (applink.startsWith(ApplinkConst.DISCOVERY_SEARCH))
        getModifiedSearchResultApplink(applink, searchParameter, activeKeyword)
    else applink
}

private fun getModifiedSearchResultApplink(
    applink: String?,
    searchParameter: Map<String, String>?,
    activeKeyword: SearchBarKeyword?,
): String {
    applink ?: return ""
    searchParameter ?: return applink

    val applinkQueryParams = URLParser(applink).paramKeyValueMap

    applinkQueryParams[SearchApiConst.PREVIOUS_KEYWORD] = searchParameter[SearchApiConst.PREVIOUS_KEYWORD]

    if (searchParameter.isMps()) {
        activeKeyword ?: return applink
        modifyMpsSearchResultParameters(applinkQueryParams, searchParameter, activeKeyword)
    }

    return ApplinkConst.DISCOVERY_SEARCH + "?" + UrlParamHelper.generateUrlParamString(applinkQueryParams)
}

private fun modifyMpsSearchResultParameters(
    applinkQueryParams: MutableMap<String, String>,
    searchParameter: Map<String, String>,
    activeKeyword: SearchBarKeyword,
) {
    val suggestedKeyword = applinkQueryParams.remove(SearchApiConst.Q) ?: ""
    applinkQueryParams[SearchApiConst.ACTIVE_TAB] = SearchApiConst.ACTIVE_TAB_MPS

    val q1 = searchParameter[SearchApiConst.Q1] ?: ""
    if (q1.isNotBlank() && activeKeyword.position != 0) {
        applinkQueryParams[SearchApiConst.Q1] = q1
    } else if (activeKeyword.position == 0 && suggestedKeyword.isNotBlank()) {
        applinkQueryParams[SearchApiConst.Q1] = suggestedKeyword
    }
    val q2 = searchParameter[SearchApiConst.Q2] ?: ""
    if (q2.isNotBlank() && activeKeyword.position != 1) {
        applinkQueryParams[SearchApiConst.Q2] = q2
    } else if (activeKeyword.position == 1 && suggestedKeyword.isNotBlank()) {
        applinkQueryParams[SearchApiConst.Q2] = suggestedKeyword
    }
    val q3 = searchParameter[SearchApiConst.Q3] ?: ""
    if (q3.isNotBlank() && activeKeyword.position != 2) {
        applinkQueryParams[SearchApiConst.Q3] = q3
    } else if (activeKeyword.position == 2 && suggestedKeyword.isNotBlank()) {
        applinkQueryParams[SearchApiConst.Q3] = suggestedKeyword
    }
}

internal fun getShopIdFromApplink(applink: String): String {
    return applink.substringWithPrefixAndSuffix("tokopedia://shop/", "?")
}

private fun String.substringWithPrefixAndSuffix(prefix: String, suffix: String): String {
    val suffixIndex = indexOf(suffix)

    val startIndex = prefix.length
    val endIndex = if (suffixIndex == -1) length else suffixIndex

    return try {
        if (startsWith(prefix)) {
            substring(startIndex, endIndex)
        } else {
            ""
        }
    } catch (e: Exception) {
        ""
    }
}

internal fun getProfileIdFromApplink(applink: String): String {
    return applink.substringWithPrefixAndSuffix("tokopedia://people/", "?")
}
