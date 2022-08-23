package com.tokopedia.applink.digitaldeals

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalDeals
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL_SLUG
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

object DeeplinkMapperDeals {

    fun getRegisteredNavigationDeals(context: Context, deeplink: String) : String {
        val uri = Uri.parse(deeplink)
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        return when {
            // tokopedia://deals
            uri.pathSegments.size == 0 ->
                ApplinkConstInternalDeals.DEALS_HOMEPAGE

            //tokopedia://deals/order
            uri.pathSegments.size == 1 && uri.pathSegments[0] == "order"->
                ""
            // tokopedia://deals/{id}
            uri.pathSegments.size == 1->
                "${ApplinkConstInternalDeals.DEALS_PRODUCT_DETAIL_PAGE}?${uri.lastPathSegment}"
                    //UriUtil.buildUri(GLOBAL_INTERNAL_DIGITAL_DEAL_SLUG, uri.pathSegments[0])

            //tokopedia://deals/brand/{slug}
            uri.pathSegments.size == 2 && uri.pathSegments[0] == "brand" ->
                    "${ApplinkConstInternalDeals.DEALS_BRAND_DETAIL_PAGE}?${uri.pathSegments[1]}"

            //tokopedia://deals/i/{slug}
            uri.pathSegments.size == 2 && uri.pathSegments[0] == "i" ->
                "${ApplinkConstInternalDeals.DEALS_PRODUCT_DETAIL_PAGE}?${uri.lastPathSegment}"
                //UriUtil.buildUri(GLOBAL_INTERNAL_DIGITAL_DEAL_SLUG, uri.lastPathSegment)

            //tokopedia://deals/allbrands/{isVoucher}
            uri.pathSegments.size == 2 && uri.pathSegments[0] == "allbrands"->
                    "${ApplinkConstInternalDeals.DEALS_BRAND_PAGE}?${uri.query}"

            //tokopedia://deals/category/page
            uri.pathSegments.size == 2 ->
                "${ApplinkConstInternalDeals.DEALS_CATEGORY_PAGE}?${uri.query}"
            else -> ""
        }

    }
}