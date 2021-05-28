package com.tokopedia.applink.tokonow

import android.net.Uri
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalTokoMart

object DeeplinkMapperTokoNow {

    fun getRegisteredNavigationTokoNowSearch(deeplink: String): String {
        val uri = Uri.parse(deeplink)

        val query = uri.encodedQuery
        val queryString = if (query.isNullOrEmpty()) "" else "?" + uri.encodedQuery

        return ApplinkConstInternalTokoMart.SEARCH + queryString
    }

    fun getRegisteredNavigationTokoNowCategory(uri:Uri, idList: List<String>?): String {
        val query = uri.encodedQuery
        val queryString = if (query.isNullOrEmpty()) "" else "?" + uri.encodedQuery

        return UriUtil.buildUri(
                ApplinkConstInternalTokoMart.CATEGORY + queryString,
                idList?.getOrNull(0)
        )
    }
}