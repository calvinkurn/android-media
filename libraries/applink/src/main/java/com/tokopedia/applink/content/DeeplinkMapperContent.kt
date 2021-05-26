package com.tokopedia.applink.content

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.applink.startsWithPattern

/**
 * Created by jegul on 2019-11-04
 */
object DeeplinkMapperContent {

    fun getRegisteredNavigationContentFromHttp(uri: Uri, deepLink: String): String {
        return when {
            uri.host == ApplinkConstInternalContent.TOKOPEDIA_BYME -> handleNavigationByMe(deepLink)
            uri.pathSegments.joinToString("/").startsWith(ApplinkConstInternalContent.PLAY_PATH_LITE, false) -> handleNavigationPlay(deepLink)
            else -> ""
        }
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

    private fun handleNavigationPlay(deepLink: String): String {
        return "${ApplinkConst.BROWSER}?url=$deepLink"
    }

    private fun handleNavigationByMe(deepLink: String): String {
        return try {
            if (deepLink.startsWithPattern(ApplinkConstInternalContent.TOKOPEDIA_BYME_HTTP) ||
                    deepLink.startsWithPattern(ApplinkConstInternalContent.TOKOPEDIA_BYME_HTTPS)) {
                val path = Uri.parse(deepLink).path?.removePrefix("/").orEmpty()
                "${ApplinkConstInternalContent.AFFILIATE_BYME_TRACKING}$path"
            } else deepLink
        } catch (e: Throwable) {
            deepLink
        }
    }
}