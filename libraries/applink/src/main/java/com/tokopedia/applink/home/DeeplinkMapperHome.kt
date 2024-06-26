package com.tokopedia.applink.home

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.*
import com.tokopedia.applink.navigation.DeeplinkMapperMainNavigation.EXTRA_TAB_TYPE
import com.tokopedia.applink.navigation.DeeplinkMapperMainNavigation.TAB_TYPE_FEED
import com.tokopedia.applink.navigation.DeeplinkMapperMainNavigation.TAB_TYPE_HOME
import com.tokopedia.applink.navigation.DeeplinkNavigationUtil
import com.tokopedia.applink.startsWithPattern
import com.tokopedia.config.GlobalConfig
import com.tokopedia.user.session.UserSession

object DeeplinkMapperHome {

    const val EXTRA_ACCOUNT_TAB = "ACCOUNT_TAB"
    const val EXTRA_ACCOUNT_TAB_VALUE_SELLER = "ACCOUNT_TAB_SELLER"
    const val EXTRA_RECOMMEND_LIST = "recommend_list"

    const val TAB_POSITION_FEED = 1
    const val EXTRA_TAB_POSITION = "TAB_POSITION"
    const val TAB_POSITION_ACCOUNT = 4
    const val TAB_POSITION_OS = 2
    const val TAB_POSITION_RECOM = 5

    private val deeplinkNavigationUtil by lazy { DeeplinkNavigationUtil() }

    fun isLoginAndHasShop(context: Context): Boolean {
        val userSession = UserSession(context)
        return userSession.isLoggedIn && userSession.hasShop()
    }

    fun getRegisteredNavigationHome(context: Context, deeplink: String): String {
        if (GlobalConfig.isSellerApp()) {
            return if (isLoginAndHasShop(context)) {
                ApplinkConstInternalSellerapp.SELLER_HOME
            } else {
                ApplinkConstInternalSellerapp.WELCOME
            }
        }
        val uri = Uri.parse(deeplink)

        // tokopedia://home
        if (uri.host == Uri.parse(ApplinkConst.HOME).host && uri.pathSegments.isEmpty()) {
            return if (deeplinkNavigationUtil.newHomeNavEnabled()) {
                UriUtil.buildUriAppendParams(
                    ApplinkConsInternalHome.HOME_NAVIGATION,
                    mapOf(EXTRA_TAB_TYPE to TAB_TYPE_HOME)
                )
            } else {
                ApplinkConsInternalHome.HOME_NAVIGATION_OLD
            }
        } else if (deeplink.startsWith(ApplinkConst.HOME_OLD) && uri.pathSegments.size == 1) {
            return ApplinkConsInternalHome.HOME_NAVIGATION_OLD
        } else if (deeplink.startsWith(ApplinkConst.HOME_CATEGORY) && uri.pathSegments.size == 1) {
            return ApplinkConsInternalHome.HOME_NAVIGATION
        } else if (deeplink.startsWith(ApplinkConst.Navigation.MAIN_NAV)) {
            return ApplinkConsInternalNavigation.MAIN_NAVIGATION
        } else if (deeplink.startsWith(ApplinkConst.HOME_FEED) && uri.pathSegments.size == 1) {
            return UriUtil.buildUriAppendParams(ApplinkConsInternalHome.HOME_NAVIGATION, mapOf(
                EXTRA_TAB_TYPE to TAB_TYPE_FEED))
        } else if (deeplink.startsWith(ApplinkConst.HOME_ACCOUNT_SELLER) && uri.pathSegments.size == 2) {
            return ApplinkConstInternalUserPlatform.NEW_HOME_ACCOUNT
        } else if (deeplink.startsWith(ApplinkConst.HOME_ACCOUNT) && uri.pathSegments.size == 1) {
            return ApplinkConstInternalUserPlatform.NEW_HOME_ACCOUNT
        } else if (deeplink.startsWith(ApplinkConst.HOME_RECOMMENDATION) && uri.pathSegments.size == 1) {
            return UriUtil.buildUriAppendParams(
                ApplinkConsInternalHome.HOME_NAVIGATION,
                mapOf(EXTRA_TAB_POSITION to TAB_POSITION_RECOM, EXTRA_RECOMMEND_LIST to true)
            )
        }
        return deeplink
    }

    fun getRegisteredNavigationHomeOfficialStore(deeplink: String): String {
        val uri = Uri.parse(deeplink)

        // tokopedia://official-store
        if (uri.host == Uri.parse(ApplinkConst.OFFICIAL_STORE).host && uri.pathSegments.isEmpty()) {
            return ApplinkConstInternalDiscovery.SOS
        } else if (deeplink.startsWith(ApplinkConst.OFFICIAL_STORES) && uri.pathSegments.isEmpty()) {
            return ApplinkConstInternalDiscovery.SOS
        } else if (deeplink.startsWith(ApplinkConst.BRAND_LIST)) {
            return getBrandlistInternal(deeplink)
        } else if (deeplink.startsWithPattern(ApplinkConst.OFFICIAL_STORE_CATEGORY) && uri.pathSegments.size == 1) {
            return ApplinkConstInternalDiscovery.SOS
        }
        return deeplink
    }

    private fun getBrandlistInternal(deeplink: String): String {
        val parsedUri = Uri.parse(deeplink)
        val segments = parsedUri.pathSegments

        val categoryId = if (segments.size > 1) segments.last() else "0"
        return UriUtil.buildUri(ApplinkConstInternalMechant.BRANDLIST, categoryId)
    }

    fun getRegisteredExplore(deeplink: String): String {
        val uri = Uri.parse(deeplink)
        return when {
            uri.pathSegments.size > 0 -> ApplinkConsInternalHome.EXPLORE + uri.path
            else -> ApplinkConsInternalHome.EXPLORE
        }
    }

    fun getRegisteredInboxNavigation(deeplink: String): String {
        return ApplinkConsInternalHome.HOME_INBOX
    }
}
