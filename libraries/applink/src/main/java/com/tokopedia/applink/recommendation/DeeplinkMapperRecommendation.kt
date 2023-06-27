package com.tokopedia.applink.recommendation

import android.net.Uri
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConsInternalHome
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.EMPTY
import java.net.URLDecoder

object DeeplinkMapperRecommendation{
    fun getRegisteredNavigationRecommendation(deeplink: String): String {
        val uri = Uri.parse(deeplink)
        return when {
            uri.pathSegments.size > 0 -> ApplinkConsInternalHome.DEFAULT_HOME_RECOMMENDATION + uri.path
            else -> ApplinkConsInternalHome.DEFAULT_HOME_RECOMMENDATION
        }
    }

    private const val RECOM_PRODUCT_ID = "recomProdId"
    fun getRegisteredNavigationRecommendationFromHttp(uri: Uri): String {
        try {
            val decodedUrl = URLDecoder.decode(uri.toString(), Charsets.UTF_8.name())
            val pattern = "^https://www\\.tokopedia\\.com(/(rekomendasi))".toRegex()
            val result = pattern.find(decodedUrl)
            val matches = result?.groupValues
            if (!matches.isNullOrEmpty()) {
                return redirectToDiscoveryRecom(uri)
            }
        } catch (e: Exception) {
            return String.EMPTY
        }
        return String.EMPTY
    }

    fun redirectToDiscoveryRecom(uri: Uri): String {
        return if (uri.pathSegments.size > 0) {
            val applink = Uri.Builder()
                .scheme(DeeplinkConstant.SCHEME_INTERNAL)
                .authority(ApplinkConstInternalGlobal.HOST_GLOBAL)
                .appendPath(ApplinkConsInternalHome.AUTHORITY_DISCOVERY)
                .appendPath(ApplinkConsInternalHome.PATH_REKOMENDASI)
                .appendQueryParameter(RECOM_PRODUCT_ID, uri.pathSegments[1])
                .build()
                .toString()
            if (uri.query != null) {
                return applink + "&${uri.query}"
            } else {
                return applink
            }
        } else {
            Uri.Builder()
                .scheme(DeeplinkConstant.SCHEME_INTERNAL)
                .authority(ApplinkConstInternalGlobal.HOST_GLOBAL)
                .appendPath(ApplinkConsInternalHome.AUTHORITY_DISCOVERY)
                .appendPath(ApplinkConsInternalHome.PATH_REKOMENDASI).toString()
        }
    }
}
