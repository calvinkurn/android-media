package com.tokopedia.applink.tokonow

import android.net.Uri
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow

object DeeplinkMapperTokopediaNow {

    fun getRegisteredNavigationTokopediaNowSearch(deeplink: String): String {
        val uri = Uri.parse(deeplink)

        val query = uri.encodedQuery
        val queryString = if (query.isNullOrEmpty()) "" else "?" + uri.encodedQuery

        return ApplinkConstInternalTokopediaNow.SEARCH + queryString
    }

    fun getRegisteredNavigationTokopediaNowCategory(uri:Uri, idList: List<String>?): String {
        val query = uri.encodedQuery
        val queryString = if (query.isNullOrEmpty()) "" else "?" + uri.encodedQuery

        return UriUtil.buildUri(
                ApplinkConstInternalTokopediaNow.CATEGORY + queryString,
                idList?.getOrNull(0)
        )
    }
}