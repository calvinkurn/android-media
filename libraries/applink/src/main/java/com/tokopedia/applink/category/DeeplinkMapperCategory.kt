package com.tokopedia.applink.category

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalCategory
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

object DeeplinkMapperCategory {
    fun getRegisteredCategoryNavigation(uri: Uri): String {
        val segmentList = uri.pathSegments
        if (segmentList.size == 0) return ""
        var identifier: String? = null
        for (segment in segmentList.indices) {
            identifier = if (segment == 0) {
                segmentList[segment]
            } else {
                identifier + "_" + segmentList[segment]
            }
        }

        return UriUtil.buildUri(ApplinkConstInternalCategory.INTERNAL_CATEGORY_DETAIL, identifier)
    }

    fun getNewExploreCategoryNavigation(deeplink: String): String {
        val abTestPlatform = RemoteConfigInstance.getInstance().abTestPlatform
        val isNewExploreCategory = abTestPlatform.getString(RollenceKey.JELAJAH_REVAMP, RollenceKey.EXPLORE_CATEGORY_DEFAULT) == RollenceKey.EXPLORE_CATEGORY_EXP
        return if (isNewExploreCategory) {
            ApplinkConstInternalDiscovery.INTERNAL_HOME_EXPLORE_CATEGORY
        } else {
            deeplink.replace(ApplinkConst.Digital.DIGITAL_BROWSE, ApplinkConstInternalCategory.INTERNAL_EXPLORE_CATEGORY)
        }
    }

    fun getRegisteredNavigationExploreCategory(deeplink: String): String {
        val TYPE_LAYANAN = 2
        val TYPE_BELANJA = 1
        val uri = Uri.parse(deeplink)
        return when (uri.getQueryParameter("type")?.toInt()) {
            TYPE_LAYANAN -> {
                getNewExploreCategoryNavigation(deeplink)
            }
            TYPE_BELANJA -> {
                deeplink.replace(ApplinkConst.Digital.DIGITAL_BROWSE, ApplinkConstInternalCategory.INTERNAL_BELANJA_CATEGORY)
            }
            else -> {
                deeplink
            }
        }
    }

    fun getRegisteredNavigationCatalog(deeplink: String): String {
        return deeplink.replace(DeeplinkConstant.SCHEME_TOKOPEDIA, DeeplinkConstant.SCHEME_INTERNAL)
    }

    fun getRegisteredNavigationCatalogProductList(deeplink: String): String {
        return deeplink.replace(DeeplinkConstant.SCHEME_TOKOPEDIA, DeeplinkConstant.SCHEME_INTERNAL)
    }

    fun getRegisteredNavigationAffiliate(deeplink: String): String {
        return deeplink.replace(DeeplinkConstant.SCHEME_TOKOPEDIA, DeeplinkConstant.SCHEME_INTERNAL)
    }
    fun getRegisteredNavigationAffiliateFromHttp(uri: Uri): String {
        return UriUtil.buildUri("${DeeplinkConstant.SCHEME_INTERNAL}:/${uri.path}")
    }

    fun getRegisteredNavigationCategory(deeplink: String): String {
        return deeplink.replace(DeeplinkConstant.SCHEME_TOKOPEDIA, DeeplinkConstant.SCHEME_INTERNAL)
    }

    fun getRegisteredTradeinNavigation(deeplink: String): String {
        return deeplink.replace(DeeplinkConstant.SCHEME_TOKOPEDIA, DeeplinkConstant.SCHEME_INTERNAL)
    }
}
