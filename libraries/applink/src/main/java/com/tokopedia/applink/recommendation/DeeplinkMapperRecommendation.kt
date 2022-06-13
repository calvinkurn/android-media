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
fun getRegisteredNavigationRecommendationFromHttp(uri: Uri): String {
    return when {
        uri.pathSegments.size > 0 -> {
            Uri.Builder()
                .scheme(DeeplinkConstant.SCHEME_TOKOPEDIA)
                .authority(ApplinkConsInternalHome.AUTHORITY_DISCOVERY)
                .appendPath(ApplinkConsInternalHome.PATH_REKOMENDASI)
                .appendQueryParameter(RECOM_PRODUCT_ID, uri.pathSegments[1])
                .build()
                .toString() + "?${uri.query}"
        }
        else -> Uri.Builder()
            .scheme(DeeplinkConstant.SCHEME_TOKOPEDIA)
            .authority(ApplinkConsInternalHome.AUTHORITY_DISCOVERY)
            .appendPath(ApplinkConsInternalHome.PATH_REKOMENDASI).toString()
    }
}
