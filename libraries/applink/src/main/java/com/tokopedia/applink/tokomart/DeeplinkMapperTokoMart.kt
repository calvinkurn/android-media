package com.tokopedia.applink.tokomart

import android.net.Uri
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalTokoMart

object DeeplinkMapperTokoMart {

    fun getRegisteredNavigationTokoMartSearch(deeplink: String): String {
        val uri = Uri.parse(deeplink)

        val query = uri.encodedQuery
        val queryString = if (query.isNullOrEmpty()) "" else "?" + uri.encodedQuery

        return ApplinkConstInternalTokoMart.SEARCH + queryString
    }

    fun getRegisteredNavigationTokoMartCategory(uri:Uri, idList: List<String>?): String {
        val query = uri.encodedQuery
        val queryString = if (query.isNullOrEmpty()) "" else "?" + uri.encodedQuery

        return UriUtil.buildUri(
                ApplinkConstInternalTokoMart.CATEGORY + queryString,
                idList?.getOrNull(0)
        )
    }
}