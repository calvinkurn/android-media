package com.tokopedia.applink.content

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.applink.internal.ApplinkConstInternalContent.INTERNAL_PRODUCT_PICKER_FROM_SHOP
import com.tokopedia.applink.internal.ApplinkConstInternalContent.INTERNAL_AFFILIATE_CREATE_POST_V2
import com.tokopedia.applink.startsWithPattern

/**
 * Created by jegul on 2019-11-04
 */
object DeeplinkMapperContent {

    fun getRegisteredNavigationContentFromHttp(uri: Uri, deepLink: String): String {
        return if (uri.pathSegments
                .joinToString("/")
                .startsWith(ApplinkConstInternalContent.PLAY_PATH_LITE, false)) {
            handleNavigationPlay(uri)
        } else ""
    }

    /**
     * Replace "tokopedia" scheme to "tokopedia-android-internal"
     * This method keeps the query parameters intact on the deeplink
     */
    fun getRegisteredNavigation(deeplink: String): String {
        return deeplink.replace(DeeplinkConstant.SCHEME_TOKOPEDIA, DeeplinkConstant.SCHEME_INTERNAL)
    }

    fun isPostDetailDeepLink(uri: Uri): Boolean {
        val lastPathSegment = uri.lastPathSegment?.toIntOrNull()
        return uri.host == ApplinkConstInternalContent.HOST_CONTENT && uri.pathSegments.size == 1 && lastPathSegment != null
    }

    fun getKolDeepLink(deepLink: String): String {
        when {
            deepLink.startsWith(ApplinkConst.CONTENT_CREATE_POST) -> {
                return ApplinkConstInternalContent.INTERNAL_CONTENT_CREATE_POST
            }
            deepLink.startsWithPattern(ApplinkConst.KOL_COMMENT) -> {
                return deepLink.replace(ApplinkConst.KOL_COMMENT.substringBefore("{"), ApplinkConstInternalContent.COMMENT.substringBefore("{")).plus(ApplinkConstInternalContent.COMMENT_EXTRA_PARAM)
            }
            deepLink.startsWithPattern(ApplinkConst.CONTENT_DETAIL) -> {
                return deepLink.replace(ApplinkConst.CONTENT_DETAIL.substringBefore("{"), ApplinkConstInternalContent.INTERNAL_CONTENT_POST_DETAIL)
            }
        }
        return getRegisteredNavigation(deepLink)
    }

    fun getContentCreatePostDeepLink(deepLink: String): String {
        val uri = Uri.parse(deepLink)
        if(deepLink.startsWith(ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2)){

                val regexExp = "${ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2}/?".toRegex()
                return deepLink.replace(regexExp, INTERNAL_AFFILIATE_CREATE_POST_V2)

        }

        if(deepLink.startsWithPattern(ApplinkConst.AFFILIATE_PRODUCT_PICKER_FROM_SHOP_NO_PARAM)){
                val regexExp = "${ApplinkConst.AFFILIATE_PRODUCT_PICKER_FROM_SHOP_NO_PARAM}/?".toRegex()
                return deepLink.replace(regexExp, INTERNAL_PRODUCT_PICKER_FROM_SHOP)
        }

        when {
            deepLink.startsWith(ApplinkConst.CONTENT_CREATE_POST) ||
                    deepLink.startsWithPattern(ApplinkConst.CONTENT_DRAFT_POST) ||
                    deepLink.startsWith(ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST) ||
                    deepLink.startsWithPattern(ApplinkConst.AFFILIATE_DRAFT_POST) -> {
                return getRegisteredNavigation(deepLink)
            }
        }
        return deepLink
    }

    fun getWebHostWebViewLink(deeplink : String): String{
        return deeplink.replace("tokopedia://", "https://")
    }

    private fun handleNavigationPlay(uri: Uri): String {
        return "${ApplinkConstInternalContent.INTERNAL_PLAY}/${uri.lastPathSegment}"
    }
}