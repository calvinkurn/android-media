package com.tokopedia.applink.category

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalCategory
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

    fun getRegisteredNavigationExploreCategory(deeplink: String): String {
        val TYPE_LAYANAN = 2
        val TYPE_BELANJA = 1
        val uri = Uri.parse(deeplink)
        return when (uri.getQueryParameter("type")?.toInt()) {
            TYPE_LAYANAN -> {
                deeplink.replace(ApplinkConst.Digital.DIGITAL_BROWSE, ApplinkConstInternalCategory.INTERNAL_EXPLORE_CATEGORY)
            }
            TYPE_BELANJA -> {
                deeplink.replace(ApplinkConst.Digital.DIGITAL_BROWSE, ApplinkConstInternalCategory.INTERNAL_BELANJA_CATEGORY)
            }
            else -> {
                deeplink
            }
        }
    }
}