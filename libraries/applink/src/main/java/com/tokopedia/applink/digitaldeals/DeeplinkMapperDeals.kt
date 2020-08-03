package com.tokopedia.applink.digitaldeals

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalDeals
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL_ALL_BRANDS
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL_BRAND_DETAIL
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL_SLUG
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey.MAINAPP_ENABLE_NEW_DEALS_REVAMP_FLOW

object DeeplinkMapperDeals {

    fun getRegisteredNavigationDeals(context: Context, deeplink: String) : String {
        val uri = Uri.parse(deeplink)
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        return when {
            // tokopedia://deals
            uri.pathSegments.size == 0 -> {
                if (remoteConfig.getBoolean(MAINAPP_ENABLE_NEW_DEALS_REVAMP_FLOW)) {
                    ApplinkConstInternalDeals.DEALS_HOMEPAGE
                }
                else ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL
            }

            //tokopedia://deals/order
            uri.pathSegments.size == 1 && uri.pathSegments[0] == "order"->
                ""
            // tokopedia://deals/{id}
            uri.pathSegments.size == 1->
                UriUtil.buildUri(GLOBAL_INTERNAL_DIGITAL_DEAL_SLUG, uri.pathSegments[0])

            //tokopedia://deals/brand/{slug}
            uri.pathSegments.size == 2 && uri.pathSegments[0] == "brand" ->
                UriUtil.buildUri(GLOBAL_INTERNAL_DIGITAL_DEAL_BRAND_DETAIL, uri.pathSegments[1])

            //tokopedia://deals/i/{slug}
            uri.pathSegments.size == 2 && uri.pathSegments[0] == "i" ->
                UriUtil.buildUri(GLOBAL_INTERNAL_DIGITAL_DEAL_SLUG, uri.lastPathSegment)

            //tokopedia://deals/allbrands/{isVoucher}
            uri.pathSegments.size == 2 && uri.pathSegments[0] == "allbrands"-> {
                if (remoteConfig.getBoolean(MAINAPP_ENABLE_NEW_DEALS_REVAMP_FLOW)) {
                    "${ApplinkConstInternalDeals.DEALS_BRAND_PAGE}?${uri.query}"
                }
                else UriUtil.buildUri(GLOBAL_INTERNAL_DIGITAL_DEAL_ALL_BRANDS, uri.pathSegments[1])
            }

            //tokopedia://deals/category/page
            uri.pathSegments.size == 2 -> {
                if (remoteConfig.getBoolean(MAINAPP_ENABLE_NEW_DEALS_REVAMP_FLOW)) {
                    "${ApplinkConstInternalDeals.DEALS_CATEGORY_PAGE}?${uri.query}"
                }
                else ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL_CATEGORY
            }

            else -> ""
        }

    }
}