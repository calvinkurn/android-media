package com.tokopedia.applink.search

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery

object DeeplinkMapperSearch {

    fun getRegisteredNavigationSearch(deeplink: String): String {
        val uri = Uri.parse(deeplink)

        val query = uri.encodedQuery
        val queryString = if (query.isNullOrEmpty()) {
            ""
        } else {
            "?" + uri.encodedQuery
        }
        if (deeplink.startsWith(ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE))
            return ApplinkConstInternalDiscovery.AUTOCOMPLETE + queryString
        else if (deeplink.startsWith(ApplinkConst.DISCOVERY_SEARCH))
            return ApplinkConstInternalDiscovery.SEARCH_RESULT + queryString

        return deeplink
    }
}