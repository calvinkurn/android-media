package com.tokopedia.applink.recommendation

import android.net.Uri
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConsInternalHome

fun getRegisteredNavigationRecommendation(deeplink: String): String {
    val uri = Uri.parse(deeplink)
    return when {
        uri.pathSegments.size > 0 -> ApplinkConsInternalHome.DEFAULT_HOME_RECOMMENDATION + uri.path
        else -> ApplinkConsInternalHome.DEFAULT_HOME_RECOMMENDATION
    }
}

private const val RECOM_PRODUCT_ID = "recomProductId"
fun getRegisteredNavigationRecommendationFromHttp(deeplink: String): String {
    val uri = Uri.parse(deeplink)
    return when {
        uri.pathSegments.size > 0 -> {
            ApplinkConsInternalHome.DISCOVERY_HOME_RECOMMENDATION + uri.path
            Uri.Builder()
                .scheme(DeeplinkConstant.SCHEME_TOKOPEDIA)
                .appendPath(ApplinkConsInternalHome.DISCOVERY_HOME_RECOMMENDATION)
                .appendQueryParameter(RECOM_PRODUCT_ID, uri.pathSegments[1])
                .build()
                .toString() + "?${uri.query}"
        }
        else -> ApplinkConsInternalHome.DISCOVERY_HOME_RECOMMENDATION
    }
}
