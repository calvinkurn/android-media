package com.tokopedia.applink.promo

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalPromo

fun getRegisteredNavigationTokopoints(deeplink: String) =
    when (deeplink) {
        ApplinkConst.TOKOPEDIA_REWARD, ApplinkConst.TOKOPOINTS -> ApplinkConstInternalPromo.TOKOPOINTS_HOME
        else ->
            getDynamicDeeplinkForTokopoints(deeplink)
    }

fun getDynamicDeeplinkForTokopoints(deeplink: String): String {
    val uri = Uri.parse(deeplink)
   return when {
        (deeplink.contains(ApplinkConst.TOKOPEDIA_REWARD) && uri.pathSegments.isEmpty())
                || (deeplink.contains(ApplinkConst.TOKOPOINTS) && uri.pathSegments.isEmpty()) -> {
            getSourceDeeplink(deeplink)
        }
        else -> {
            getDestinationDeeplink(deeplink)
        }
    }
}

fun getSourceDeeplink(deeplink: String): String {
   return when {
        deeplink.contains(ApplinkConst.TOKOPEDIA_REWARD) -> {
            return deeplink.replace(
                ApplinkConst.TOKOPEDIA_REWARD,
                ApplinkConstInternalPromo.TOKOPOINTS_HOME
            )
        }
        deeplink.contains(ApplinkConst.TOKOPOINTS) -> {
            return deeplink.replace(
                ApplinkConst.TOKOPOINTS,
                ApplinkConstInternalPromo.TOKOPOINTS_HOME
            )
        }
       else -> ""
   }
}

fun getDestinationDeeplink(deeplink: String): String {
    val uri = Uri.parse(deeplink)
    val deepLinkInternal: String = when {
        deeplink.contains(ApplinkConst.TOKOPOINTS) -> {
            deeplink.replace(
                ApplinkConst.TOKOPOINTS,
                ApplinkConstInternalPromo.INTERNAL_TOKOPOINTS
            )
        }
        deeplink.contains(ApplinkConst.TOKOPEDIA_REWARD) -> {
            deeplink.replace(
                ApplinkConst.TOKOPEDIA_REWARD,
                ApplinkConstInternalPromo.INTERNAL_TOKOPOINTS
            )
        }
        else -> ""
    }
    when {
        deepLinkInternal.contains(ApplinkConst.TokoPoints.COUPON_DETAIL) -> {
            return deepLinkInternal.replace(
                ApplinkConst.TokoPoints.COUPON_DETAIL,
                ApplinkConst.TokoPoints.COUPON_DETAIL_VALUE
            )
        }
        deepLinkInternal.contains(ApplinkConst.TokoPoints.CATALOG_DETAIL_NEW) -> {
            return deepLinkInternal.replace(
                ApplinkConst.TokoPoints.CATALOG_DETAIL_NEW,
                ApplinkConst.TokoPoints.CATALOG_DETAIL_VALUE
            )
        }
        deepLinkInternal.contains(ApplinkConst.TokoPoints.CATALOG_DETAIL) -> {
            return deepLinkInternal.replace(
                ApplinkConst.TokoPoints.CATALOG_DETAIL,
                ApplinkConst.TokoPoints.CATALOG_DETAIL_VALUE
            )
        }
        deepLinkInternal.contains(ApplinkConst.TokoPoints.CATALOG_LIST_NEW) &&
                uri.pathSegments[0] == ApplinkConst.TokoPoints.CATALOG_LIST_NEW -> {
            return deepLinkInternal.replace(
                ApplinkConst.TokoPoints.CATALOG_LIST_NEW,
                ApplinkConst.TokoPoints.CATALOG_LIST_VALUE
            )
        }
        else -> {
        }
    }
    return deepLinkInternal
}
