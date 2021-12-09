package com.tokopedia.applink.find

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import tokopedia.applink.R

object DeepLinkMapperFind {
    fun getRegisteredNavigationFindFromHttp(context: Context, uri: Uri): String {
        val segments = uri.pathSegments
        if (segments.joinToString("/").startsWith(context.getString(R.string.host_find), false) ||
                segments.joinToString("/").startsWith(context.getString(R.string.host_amp_find), false)) {

            val searchSegments = getPathAfter(segments, "find")
            var query = ""
            if (searchSegments.isNotEmpty()) {
                val city = if (searchSegments.indexOf("c") == 1) "-di-" + searchSegments[searchSegments.lastIndex] else ""
                query = searchSegments[0] + city
            }

            return getRegisteredFind(ApplinkConst.FIND + "/" + query)
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

    private fun getPathAfter(segments: List<String>, path: String): List<String> {
        val indexOfFindSegment = segments.indexOf(path)
        return segments.filterIndexed { index, _ ->
            index > indexOfFindSegment
        }
    }
}