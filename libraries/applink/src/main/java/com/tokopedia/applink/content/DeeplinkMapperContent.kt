package com.tokopedia.applink.content

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.home.DeeplinkMapperHome
import com.tokopedia.applink.internal.ApplinkConsInternalHome
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.applink.internal.ApplinkConstInternalContent.ARGS_FEED_VIDEO_TAB_SELECT_CHIP
import com.tokopedia.applink.internal.ApplinkConstInternalContent.EXTRA_FEED_TAB_POSITION
import com.tokopedia.applink.internal.ApplinkConstInternalContent.INTERNAL_AFFILIATE_CREATE_POST_V2
import com.tokopedia.applink.internal.ApplinkConstInternalContent.INTERNAL_FEED_CREATION_PRODUCT_SEARCH
import com.tokopedia.applink.internal.ApplinkConstInternalContent.INTERNAL_FEED_CREATION_SHOP_SEARCH
import com.tokopedia.applink.internal.ApplinkConstInternalContent.INTERNAL_PRODUCT_PICKER_FROM_SHOP
import com.tokopedia.applink.internal.ApplinkConstInternalContent.TAB_POSITION_EXPLORE
import com.tokopedia.applink.internal.ApplinkConstInternalContent.TAB_POSITION_VIDEO
import com.tokopedia.applink.startsWithPattern
import com.tokopedia.config.GlobalConfig

/**
 * Created by jegul on 2019-11-04
 */
object DeeplinkMapperContent {

    fun getRegisteredNavigationContentFromHttp(uri: Uri, deepLink: String): String {
        return if (uri.pathSegments
            .joinToString("/")
            .startsWith(ApplinkConstInternalContent.PLAY_PATH_LITE, false)
        ) {
            handleNavigationPlay(uri)
        } else {
            ""
        }
    }

    fun getRegisteredNavigationFeedVideoFromHttp(uri: Uri, deepLink: String): String {
        return if (uri.pathSegments
            .joinToString("/")
            .startsWith(ApplinkConstInternalContent.FEED_VIDEO, false)
        ) {
            handleNavigationFeedVideo(uri)
        } else {
            ""
        }
    }

    /**
     * Replace "tokopedia" scheme to "tokopedia-android-internal"
     * This method keeps the query parameters intact on the deeplink
     */
    fun getProfileDeeplink(deepLink: String): String {
        return if (GlobalConfig.isSellerApp()) {
            getProfileSellerAppDeepLink()
        } else {
            getRegisteredNavigation(deepLink)
        }
    }

    fun getRegisteredNavigation(deeplink: String): String {
        return deeplink.replace(DeeplinkConstant.SCHEME_TOKOPEDIA, DeeplinkConstant.SCHEME_INTERNAL)
    }

    fun isPostDetailDeepLink(uri: Uri): Boolean {
        val lastPathSegment = uri.lastPathSegment?.toIntOrNull()
        return uri.host == ApplinkConstInternalContent.HOST_CONTENT && uri.pathSegments.size == 1 && lastPathSegment != null
    }

    fun getKolDeepLink(deepLink: String): String {
        when {
            deepLink.startsWithPattern(ApplinkConst.CONTENT_DETAIL) -> {
                return deepLink.replace(ApplinkConst.CONTENT_DETAIL.substringBefore("{"), ApplinkConstInternalContent.INTERNAL_CONTENT_POST_DETAIL)
            }
        }
        return getRegisteredNavigation(deepLink)
    }

    fun getContentCreatePostDeepLink(deepLink: String): String {
        if (deepLink.startsWith(ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2)) {
            val regexExp = "${ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2}/?".toRegex()
            return deepLink.replace(regexExp, INTERNAL_AFFILIATE_CREATE_POST_V2)
        }

        if (deepLink.startsWithPattern(ApplinkConst.AFFILIATE_PRODUCT_PICKER_FROM_SHOP_NO_PARAM)) {
            val regexExp = "${ApplinkConst.AFFILIATE_PRODUCT_PICKER_FROM_SHOP_NO_PARAM}/?".toRegex()
            return deepLink.replace(regexExp, INTERNAL_PRODUCT_PICKER_FROM_SHOP)
        }

        if (deepLink.startsWithPattern(ApplinkConst.FEED_CREATION_PRODUCT_SEARCH)) {
            val regexExp = "${ApplinkConst.FEED_CREATION_PRODUCT_SEARCH}/?".toRegex()
            return deepLink.replace(regexExp, INTERNAL_FEED_CREATION_PRODUCT_SEARCH)
        }

        if (deepLink.startsWithPattern(ApplinkConst.FEED_CREATION_SHOP_SEARCH)) {
            val regexExp = "${ApplinkConst.FEED_CREATION_SHOP_SEARCH}".toRegex()
            return deepLink.replace(regexExp, INTERNAL_FEED_CREATION_SHOP_SEARCH)
        }

        return deepLink
    }

    fun getWebHostWebViewLink(deeplink: String): String {
        return deeplink.replace("tokopedia://", "https://")
    }

    private fun handleNavigationPlay(uri: Uri): String {
        return "${ApplinkConstInternalContent.INTERNAL_PLAY}/${uri.lastPathSegment}"
    }
    private fun handleNavigationFeedVideo(uri: Uri): String {
        val finalDeeplink = "${ApplinkConst.FEED_VIDEO}?${uri.query}"
        return getRegisteredNavigationHomeFeedVideo(finalDeeplink)
    }

    /**
     * For easier hansel purpose
     */
    private fun getProfileSellerAppDeepLink(): String {
        return ApplinkConstInternalContent.INTERNAL_FEATURE_PREVENTION
    }

    fun getRegisteredNavigationHomeFeedExplore(): String {
        return UriUtil.buildUriAppendParams(
            ApplinkConsInternalHome.HOME_NAVIGATION,
            mapOf(
                DeeplinkMapperHome.EXTRA_TAB_POSITION to DeeplinkMapperHome.TAB_POSITION_FEED,
                EXTRA_FEED_TAB_POSITION to TAB_POSITION_EXPLORE
            )
        )
    }
    fun getRegisteredNavigationHomeFeedVideo(deeplink: String): String {
        val selectedChip =
            Uri.parse(deeplink).getQueryParameter(ARGS_FEED_VIDEO_TAB_SELECT_CHIP) ?: ""
        return UriUtil.buildUriAppendParams(
            ApplinkConsInternalHome.HOME_NAVIGATION,
            mapOf(
                DeeplinkMapperHome.EXTRA_TAB_POSITION to DeeplinkMapperHome.TAB_POSITION_FEED,
                EXTRA_FEED_TAB_POSITION to TAB_POSITION_VIDEO,
                ARGS_FEED_VIDEO_TAB_SELECT_CHIP to selectedChip
            )
        )
    }
}
