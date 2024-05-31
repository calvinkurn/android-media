package com.tokopedia.applink.content

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.home.DeeplinkMapperHome
import com.tokopedia.applink.internal.ApplinkConsInternalHome
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.applink.internal.ApplinkConstInternalContent.INTERNAL_AFFILIATE_CREATE_POST_V2
import com.tokopedia.applink.internal.ApplinkConstInternalContent.INTERNAL_FEED_CREATION_PRODUCT_SEARCH
import com.tokopedia.applink.internal.ApplinkConstInternalContent.INTERNAL_FEED_CREATION_SHOP_SEARCH
import com.tokopedia.applink.internal.ApplinkConstInternalContent.INTERNAL_FEED_LOCAL_BROWSE
import com.tokopedia.applink.internal.ApplinkConstInternalContent.INTERNAL_FEED_SEARCH_RESULT
import com.tokopedia.applink.internal.ApplinkConstInternalContent.INTERNAL_PRODUCT_PICKER_FROM_SHOP
import com.tokopedia.applink.internal.ApplinkConstInternalContent.UF_EXTRA_FEED_WIDGET_ID
import com.tokopedia.applink.internal.ApplinkConstInternalContent.UF_EXTRA_FEED_ENTRY_POINT
import com.tokopedia.applink.internal.ApplinkConstInternalContent.UF_EXTRA_FEED_SOURCE_ID
import com.tokopedia.applink.internal.ApplinkConstInternalContent.UF_EXTRA_FEED_SOURCE_NAME
import com.tokopedia.applink.internal.ApplinkConstInternalContent.UF_EXTRA_FEED_TAB_NAME
import com.tokopedia.applink.navigation.DeeplinkMapperMainNavigation.EXTRA_TAB_TYPE
import com.tokopedia.applink.navigation.DeeplinkMapperMainNavigation.TAB_TYPE_FEED
import com.tokopedia.applink.navigation.DeeplinkNavigationUtil
import com.tokopedia.applink.startsWithPattern
import com.tokopedia.config.GlobalConfig

/**
 * Created by jegul on 2019-11-04
 */
object DeeplinkMapperContent {

    private const val EXTRA_SOURCE_NAME = "source"
    private const val EXTRA_WIDGET_ID = "widget_id"
    private const val EXTRA_ENTRY_POINT = "entrypoint"

    private val deeplinkNavigationUtil by lazy { DeeplinkNavigationUtil() }

    /**
     * https://www.tokopedia.com/
     */
    fun getNavContentFromHttp(uri: Uri, deepLink: String): String {
        val pathSegments = uri.pathSegments.joinToString("/")
        return if (pathSegments.startsWith(ApplinkConstInternalContent.PLAY_PATH_LITE, false)) {
            goToAppLinkPlayInternal(uri)
        } else if (pathSegments.startsWith("feed", false) ||
            pathSegments.startsWith("content", false)
        ) {
            if (pathSegments.startsWith("feed/detail/", false) ||
                pathSegments.startsWith("content/detail/", false)
            ) {
                goToAppLinkFeedDetailInternal(uri)
            } else {
                goToAppLinkFeedHomeInternal(uri)
            }
        } else {
            ""
        }
    }

    /**
     * tokopedia://content/
     * tokopedia://feed/
     */
    fun getNavContentFromAppLink(deepLink: String): String {
        val uri = Uri.parse(deepLink)
        val pathSegments = uri.pathSegments.joinToString("/")
        return if (pathSegments.startsWith("detail/", false)) {
            goToAppLinkFeedDetailInternal(uri)
        } else if (pathSegments.startsWith("creation-product-search", false)) {
            INTERNAL_FEED_CREATION_PRODUCT_SEARCH
        } else if (pathSegments.startsWith("creation-shop-search", false)) {
            INTERNAL_FEED_CREATION_SHOP_SEARCH
        } else if (pathSegments.startsWith("hashtag", false)) {
            ""
        } else if (pathSegments.startsWith("search-result", false)) {
            INTERNAL_FEED_SEARCH_RESULT
        } else if (pathSegments.startsWith("search", false)) {
            INTERNAL_FEED_LOCAL_BROWSE
        } else if (pathSegments.startsWith("old", false)) {
            goToAppLinkFeedHomeInternal(uri, isOldNav = true)
        } else {
            goToAppLinkFeedHomeInternal(uri)
        }
    }

    /**
     * /play
     * /play/{channelId}
     * /play/channel/{channelId}
     */
    private fun goToAppLinkPlayInternal(uri: Uri): String {
        return "${ApplinkConstInternalContent.INTERNAL_PLAY}/${uri.lastPathSegment}"
    }

    /**
     *
     * /content
     * /feed
     * /content/{postId}
     * /feed/{postId}
     *
     * ?tab={tab_name}
     * ?source={source_name}
     */
    private fun goToAppLinkFeedHomeInternal(uri: Uri, isOldNav: Boolean = false): String {
        return UriUtil.buildUriAppendParams(
            uri = if (!isOldNav && deeplinkNavigationUtil.newHomeNavEnabled()) {
                ApplinkConsInternalHome.HOME_NAVIGATION
            } else {
                ApplinkConsInternalHome.HOME_NAVIGATION_OLD
            },
            queryParameters = buildMap {
                if (!isOldNav && deeplinkNavigationUtil.newHomeNavEnabled()) {
                    put(EXTRA_TAB_TYPE, TAB_TYPE_FEED)
                } else {
                    put(DeeplinkMapperHome.EXTRA_TAB_POSITION, DeeplinkMapperHome.TAB_POSITION_FEED)
                }

                val sourceName = uri.getQueryParameter(EXTRA_SOURCE_NAME)
                if (sourceName != null) put(UF_EXTRA_FEED_SOURCE_NAME, sourceName)

                val tabName = getActiveFeedTab(uri)
                if (tabName != null) {
                    put(UF_EXTRA_FEED_TAB_NAME, tabName)
                }

                val widgetId = uri.getQueryParameter(EXTRA_WIDGET_ID)
                if (widgetId != null) put(UF_EXTRA_FEED_WIDGET_ID, widgetId)

                val entryPoint = uri.getQueryParameter(EXTRA_ENTRY_POINT)
                if (entryPoint != null) put(UF_EXTRA_FEED_ENTRY_POINT, entryPoint)

                val postId = uri.lastPathSegment?.toIntOrNull() ?: return@buildMap
                put(UF_EXTRA_FEED_SOURCE_ID, postId)
            }
        )
    }

    /**
     *
     * *backward compatibility*
     * /content/explore
     * /content/following
     * /feed/explore
     * /feed/video
     */
    private fun getActiveFeedTab(uri: Uri): String? {
        return if (uri.pathSegments.contains("video")) {
            "video"
        } else if (uri.pathSegments.contains("explore")) {
            "explore"
        } else if (uri.pathSegments.contains("following")) {
            "following"
        } else {
            uri.getQueryParameter("tab")
        }
    }

    /**
     *
     * /content/detail/{postId}
     * /feed/detail/{postId}
     *
     * ?source={source_name}
     */
    private fun goToAppLinkFeedDetailInternal(uri: Uri): String {
        return UriUtil.buildUriAppendParams(
            "${ApplinkConstInternalContent.INTERNAL_CONTENT}/detail/${uri.lastPathSegment}",
            buildMap {
                val sourceName = uri.getQueryParameter(EXTRA_SOURCE_NAME)
                if (sourceName != null) put(UF_EXTRA_FEED_SOURCE_NAME, sourceName)

                val widgetId = uri.getQueryParameter(EXTRA_WIDGET_ID)
                if (widgetId != null) put(UF_EXTRA_FEED_WIDGET_ID, widgetId)

                val entryPoint = uri.getQueryParameter(EXTRA_ENTRY_POINT)
                if (entryPoint != null) put(UF_EXTRA_FEED_ENTRY_POINT, entryPoint)
            }
        )
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

    fun getRegisteredNavigation(deeplink: String): String {
        return if (deeplink.startsWith(DeeplinkConstant.SCHEME_TOKOPEDIA)) {
            deeplink.replaceFirst(DeeplinkConstant.SCHEME_TOKOPEDIA, DeeplinkConstant.SCHEME_INTERNAL)
        } else {
            deeplink
        }
    }

    fun getWebHostWebViewLink(deeplink: String): String {
        return deeplink.replace("tokopedia://", "https://")
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

    /**
     * For easier hansel purpose
     */
    private fun getProfileSellerAppDeepLink(): String {
        return ApplinkConstInternalContent.INTERNAL_FEATURE_PREVENTION
    }
}
