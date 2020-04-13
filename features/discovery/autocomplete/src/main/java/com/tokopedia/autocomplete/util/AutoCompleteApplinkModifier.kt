package com.tokopedia.autocomplete.util

import com.tokopedia.applink.ApplinkConst
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

internal fun getModifiedApplink(applink: String?, searchParameter: Map<String, String>?): String {
    applink ?: return ""

    return if (applink.startsWith(ApplinkConst.DISCOVERY_SEARCH))
        getModifiedSearchResultApplink(applink, searchParameter)
    else applink
}

private fun getModifiedSearchResultApplink(applink: String?, searchParameter: Map<String, String>?): String {
    applink ?: return ""
    searchParameter ?: return applink

    val applinkQueryParams = URLParser(applink).paramKeyValueMap

    applinkQueryParams[SearchApiConst.PREVIOUS_KEYWORD] = searchParameter[SearchApiConst.PREVIOUS_KEYWORD]

    return ApplinkConst.DISCOVERY_SEARCH + "?" + UrlParamHelper.generateUrlParamString(applinkQueryParams)
}