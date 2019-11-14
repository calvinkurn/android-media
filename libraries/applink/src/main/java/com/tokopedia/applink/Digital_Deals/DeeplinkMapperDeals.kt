package com.tokopedia.applink.Digital_Deals

import android.net.Uri
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL_ALL_BRANDS
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL_BRAND_DETAIL
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL_SLUG
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL_SLUG_BASE

object DeeplinkMapperDeals {

    fun getRegisteredNavigationDeals(deeplink: String) : String {
        val uri = Uri.parse(deeplink)
        return when {
            // tokopedia://deals
            uri.pathSegments.size == 0->
                "tokopedia-android-internal://global/deals"
            //tokopedia://deals/order
            uri.pathSegments.size == 1 && uri.pathSegments[0] == "order"->
                ""
            // tokopedia://deals/{id}
            uri.pathSegments.size == 1->
                UriUtil.buildUri(GLOBAL_INTERNAL_DIGITAL_DEAL_SLUG, uri.pathSegments[0])
            //tokopedia://deals/brand/{slug}
            uri.pathSegments.size == 2 && uri.pathSegments[0] == "brand" ->
                UriUtil.buildUri(GLOBAL_INTERNAL_DIGITAL_DEAL_ALL_BRANDS, uri.pathSegments[1])
            //tokopedia://deals/allbrands/{isVoucher}
            uri.pathSegments.size == 2 && uri.pathSegments[0] == "allbrands"->
                UriUtil.buildUri(GLOBAL_INTERNAL_DIGITAL_DEAL_BRAND_DETAIL, uri.pathSegments[1])
            //tokopedia://deals/category/page
            uri.pathSegments.size == 2->
                ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL_CATEGORY
            else -> ""
        }

    }
}