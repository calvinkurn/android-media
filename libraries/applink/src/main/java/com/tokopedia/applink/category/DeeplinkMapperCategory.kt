package com.tokopedia.applink.category

import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace

object DeeplinkMapperCategory {
    fun getRegisteredCategoryNavigation(segmentList: List<String>): String {
        var identifier: String? = null
        for (i in segmentList.indices) {
            if (i == 0) {
                identifier = segmentList[i]
            } else {
                identifier = identifier + "_" + segmentList[i]
            }
        }
        return UriUtil.buildUri(ApplinkConstInternalMarketplace.DISCOVERY_CATEGORY_DETAIL, identifier)
    }
}