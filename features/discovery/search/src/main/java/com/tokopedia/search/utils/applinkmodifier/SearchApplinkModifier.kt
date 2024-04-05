package com.tokopedia.search.utils.applinkmodifier

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.utils.UrlParamUtils
import javax.inject.Inject

@SearchScope
class SearchApplinkModifier @Inject constructor(
    queryKeyProvider: QueryKeyProvider
): ApplinkModifier,
    QueryKeyProvider by queryKeyProvider {

    override fun modifyApplink(applink: String, enterMethod: String): String =
        if (applink.isSearchResult())
            modifyApplinkToSearchResult(applink, enterMethod)
        else applink

    private fun String.isSearchResult() =
        startsWith(ApplinkConstInternalDiscovery.SEARCH_RESULT)
            || startsWith(ApplinkConst.DISCOVERY_SEARCH)

    private fun modifyApplinkToSearchResult(applink: String, enterMethod: String): String {
        val urlParser = URLParser(applink)

        val params = urlParser.paramKeyValueMap
        params[SearchApiConst.PREVIOUS_KEYWORD] = queryKey
        params[SearchApiConst.ENTER_METHOD] = enterMethod

        return ApplinkConstInternalDiscovery.SEARCH_RESULT + "?" +
            UrlParamUtils.generateUrlParamString(params)
    }
}
