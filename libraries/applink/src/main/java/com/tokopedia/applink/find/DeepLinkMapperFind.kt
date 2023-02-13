package com.tokopedia.applink.find

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import tokopedia.applink.R

object DeepLinkMapperFind {
    fun getRegisteredNavigationFindFromHttp(context: Context, uri: Uri): String {
        val segments = uri.pathSegments
        val segmentsString = segments.joinToString("/")

        val findPrefix = "${context.getString(R.string.host_find)}/"
        val ampFindPrefix = "${context.getString(R.string.host_amp_find)}/"
        val isFind = segmentsString.startsWith(findPrefix) || segmentsString.startsWith(ampFindPrefix)

        if (!isFind) return ""

        val searchSegments = getPathAfter(segments, "find")
        val query = getQuery(searchSegments)

        return getRegisteredFind(ApplinkConst.FIND + "/" + query)
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

    private fun getQuery(searchSegments: List<String>) =
        if (searchSegments.isNotEmpty()) searchSegments.first() + getCity(searchSegments)
        else ""

    private fun getCity(searchSegments: List<String>): String {
        val cityKeyIndex = searchSegments.indexOf("c")

        return if (cityKeyIndex == 1) "-di-" + searchSegments.last()
        else ""
    }
}