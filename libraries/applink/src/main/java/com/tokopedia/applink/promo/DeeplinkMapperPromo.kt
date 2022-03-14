package com.tokopedia.applink.promo

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalPromo

fun getRegisteredNavigationTokopoints(deeplink: String) =
    when (deeplink) {
        ApplinkConst.TOKOPEDIA_REWARD -> ApplinkConstInternalPromo.TOKOPOINTS_HOME
        else ->
            getDynamicDeeplinkForTokopoints(deeplink)
    }

fun getDynamicDeeplinkForTokopoints(deeplink: String): String {

    val deepLinkInternal: Any
    val uri = Uri.parse(deeplink)
    when {
        deeplink.contains(ApplinkConst.TOKOPEDIA_REWARD) && uri.pathSegments.isEmpty() -> {
            return deeplink.replace(
                ApplinkConst.TOKOPEDIA_REWARD,
                ApplinkConstInternalPromo.TOKOPOINTS_HOME
            )
        }
        else -> {
            deepLinkInternal = deeplink.replace(
                ApplinkConst.TOKOPEDIA_REWARD,
                ApplinkConstInternalPromo.INTERNAL_TOKOPOINTS
            )
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
                deepLinkInternal.contains(ApplinkConst.TokoPoints.CATALOG_LIST_NEW) &&
                        uri.pathSegments[0] == ApplinkConst.TokoPoints.CATALOG_LIST_NEW -> {
                    return deepLinkInternal.replace(
                        ApplinkConst.TokoPoints.CATALOG_LIST_NEW,
                        ApplinkConst.TokoPoints.CATALOG_LIST_VALUE
                    )
                }
                else ->{}
            }
        }
    }
    return deepLinkInternal
}

