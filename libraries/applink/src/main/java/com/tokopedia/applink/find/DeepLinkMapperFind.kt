package com.tokopedia.applink.find

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery

object DeepLinkMapperFind {
    fun getRegisteredNavigationFindFromHttp(uri: Uri, deepLink: String): String {
        val segments = uri.pathSegments
        val searchKeyword = if (segments.size > 1) segments[1] else ""
        val city = if (segments.indexOf("c") == 2) "-di-" + segments[segments.lastIndex] else ""
        val query = searchKeyword + city

        if (deepLink.startsWith(ApplinkConst.FIND) || deepLink.startsWith(ApplinkConst.AMP_FIND)) {
            return ApplinkConst.FIND + "/" + query
        }

        return ""
    }

    fun getRegisteredFind(deepLink: String): String {
        val uri = Uri.parse(deepLink)

        val query = uri.lastPathSegment?.replace("-", "%20")
        val queryString = if (query.isNullOrEmpty() || (deepLink.startsWith(ApplinkConst.AMP_FIND) && uri.pathSegments.size < 2)) {
            ""
        } else {
            "?q=${query}&navsource=find"
        }
        if (deepLink.startsWith(ApplinkConst.FIND) || deepLink.startsWith(ApplinkConst.AMP_FIND)) {
            return ApplinkConstInternalDiscovery.SEARCH_RESULT + queryString
        }
        return deepLink
    }
}