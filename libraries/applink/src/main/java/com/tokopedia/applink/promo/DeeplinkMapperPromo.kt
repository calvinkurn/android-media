package com.tokopedia.applink.promo

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import java.util.regex.Matcher
import java.util.regex.Pattern


fun getRegisteredNavigationTokopoints(context: Context, deeplink: String) =
        when (deeplink) {
            ApplinkConst.TokoPoints.HOMEPAGE, ApplinkConst.TokoPoints.HOMEPAGE2, ApplinkConst.TokoPoints.HOMEPAGE_REWARD1,
            ApplinkConst.TokoPoints.HOMEPAGE_REWARD2 -> ApplinkConstInternalPromo.TOKOPOINTS_HOME
            ApplinkConst.TokoPoints.HISTORY -> ApplinkConstInternalPromo.TOKOPOINTS_HOME
            else ->
                getDynamicDeeplinkForTokopoints(context, deeplink)
        }


fun getDynamicDeeplinkForTokopoints(context: Context, deeplink: String): String {

    var deepLinkInternal = ""
    if (deeplink.contains(ApplinkConst.TOKOPOINTS)) {
        deepLinkInternal = deeplink.replace(ApplinkConst.TOKOPOINTS, ApplinkConstInternalPromo.INTERNAL_TOKOPOINTS)
    } else if (deeplink.contains(ApplinkConst.TOKOPEDIA_REWARD)) {
        deepLinkInternal = deeplink.replace(ApplinkConst.TOKOPEDIA_REWARD, ApplinkConstInternalPromo.INTERNAL_TOKOPOINTS)
    }
    if (deepLinkInternal.contains(ApplinkConst.TokoPoints.COUPON_DETAIL)) {
        return deepLinkInternal.replace(ApplinkConst.TokoPoints.COUPON_DETAIL, ApplinkConst.TokoPoints.COUPON_DETAIL_VALUE)
    }
    val regex = "kupon-saya/?[a-z]*".toRegex()
    if (regex.containsMatchIn(deepLinkInternal)) {
        return deepLinkInternal.replace(regex, "kupon-saya")
    }
    if (deepLinkInternal.contains(ApplinkConst.TokoPoints.CATALOG_DETAIL)) {
        return deepLinkInternal.replace(ApplinkConst.TokoPoints.CATALOG_DETAIL, ApplinkConst.TokoPoints.CATALOG_DETAIL_VALUE)
    }
    if (deepLinkInternal.contains(ApplinkConst.TokoPoints.CATALOG_DETAIL_NEW)) {
        return deepLinkInternal.replace(ApplinkConst.TokoPoints.CATALOG_DETAIL_NEW, ApplinkConst.TokoPoints.CATALOG_DETAIL_VALUE)
    }
    if (deepLinkInternal.contains(ApplinkConst.TokoPoints.CATALOG_LIST_NEW)) {
        return deepLinkInternal.replace(ApplinkConst.TokoPoints.CATALOG_LIST_NEW, ApplinkConst.TokoPoints.CATALOG_LIST_VALUE)
    }

    return deepLinkInternal
}
