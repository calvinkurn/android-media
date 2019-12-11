package com.tokopedia.applink.category

import android.net.Uri
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace

object DeeplinkMapperCategory {
    fun getRegisteredCategoryNavigation(segmentList: List<String>, deplink: String): String {
        val uri = Uri.parse(deplink)
        var identifier: String? = null
        val url: String
        for (segment in segmentList.indices) {
            identifier = if (segment == 0) {
                segmentList[segment]
            } else {
                identifier + "_" + segmentList[segment]
            }
        }
        url = if (uri.query != null) {
            UriUtil.buildUri(ApplinkConstInternalMarketplace.DISCOVERY_CATEGORY_DETAIL_QUERY, identifier, uri.query)
        } else {
            UriUtil.buildUri(ApplinkConstInternalMarketplace.DISCOVERY_CATEGORY_DETAIL, identifier)
        }
        return url
    }
}