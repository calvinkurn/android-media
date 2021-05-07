package com.tokopedia.applink.home

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConsInternalHome
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.startsWithPattern
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform

object DeeplinkMapperHome {

    const val EXTRA_TAB_POSITION = "TAB_POSITION"
    const val EXTRA_ACCOUNT_TAB = "ACCOUNT_TAB"
    const val EXTRA_ACCOUNT_TAB_VALUE_SELLER = "ACCOUNT_TAB_SELLER"
    const val EXTRA_RECOMMEND_LIST = "recommend_list"

    const val TAB_POSITION_FEED = 1
    const val TAB_POSITION_ACCOUNT = 4
    const val TAB_POSITION_OS = 2
    const val TAB_POSITION_RECOM = 5

    fun getRegisteredNavigationHome(deeplink: String): String {
        if (GlobalConfig.isSellerApp()) {
            return ApplinkConstInternalSellerapp.SELLER_HOME
        }
        val uri = Uri.parse(deeplink)

        // tokopedia://home
        if (uri.host == Uri.parse(ApplinkConst.HOME).host && uri.pathSegments.isEmpty())
            return ApplinkConsInternalHome.HOME_NAVIGATION
        else if (deeplink.startsWith(ApplinkConst.HOME_CATEGORY) && uri.pathSegments.size == 1)
            return ApplinkConsInternalHome.HOME_NAVIGATION
        else if (deeplink.startsWith(ApplinkConst.Navigation.MAIN_NAV))
            return ApplinkConsInternalNavigation.MAIN_NAVIGATION
        else if (deeplink.startsWith(ApplinkConst.HOME_FEED) && uri.pathSegments.size == 1)
            return UriUtil.buildUriAppendParams(ApplinkConsInternalHome.HOME_NAVIGATION, mapOf(EXTRA_TAB_POSITION to TAB_POSITION_FEED))
        else if (deeplink.startsWith(ApplinkConst.HOME_ACCOUNT_SELLER) && uri.pathSegments.size == 2)
            return UriUtil.buildUriAppendParams(ApplinkConsInternalHome.HOME_NAVIGATION,
                    mapOf(EXTRA_TAB_POSITION to TAB_POSITION_ACCOUNT, EXTRA_ACCOUNT_TAB to EXTRA_ACCOUNT_TAB_VALUE_SELLER))
        else if (deeplink.startsWith(ApplinkConst.HOME_ACCOUNT) && uri.pathSegments.size == 1)
            return UriUtil.buildUriAppendParams(ApplinkConsInternalHome.HOME_NAVIGATION,
                    mapOf(EXTRA_TAB_POSITION to TAB_POSITION_ACCOUNT))
        else if (deeplink.startsWith(ApplinkConst.HOME_RECOMMENDATION) && uri.pathSegments.size == 1)
            return UriUtil.buildUriAppendParams(ApplinkConsInternalHome.HOME_NAVIGATION,
                    mapOf(EXTRA_TAB_POSITION to TAB_POSITION_RECOM, EXTRA_RECOMMEND_LIST to true))
        return deeplink
    }

    fun getRegisteredNavigationHomeOfficialStore(deeplink: String): String {
        val uri = Uri.parse(deeplink)

        // tokopedia://official-store
        if (uri.host == Uri.parse(ApplinkConst.OFFICIAL_STORE).host && uri.pathSegments.isEmpty())
            return UriUtil.buildUriAppendParams(ApplinkConsInternalHome.HOME_NAVIGATION, mapOf(EXTRA_TAB_POSITION to TAB_POSITION_OS))
        else if (deeplink.startsWith(ApplinkConst.OFFICIAL_STORES) && uri.pathSegments.isEmpty())
            return UriUtil.buildUriAppendParams(ApplinkConsInternalHome.HOME_NAVIGATION, mapOf(EXTRA_TAB_POSITION to TAB_POSITION_OS))
        else if (deeplink.startsWithPattern(ApplinkConst.OFFICIAL_STORE_CATEGORY) && uri.pathSegments.size == 1)
        {
            val params = UriUtil.destructureUriToMap(ApplinkConst.OFFICIAL_STORE_CATEGORY, Uri.parse(deeplink), true)
            params[EXTRA_TAB_POSITION] = TAB_POSITION_OS

            return UriUtil.buildUriAppendParams(ApplinkConsInternalHome.HOME_NAVIGATION, params.toMap())
        }

        return deeplink
    }

    fun getRegisteredNavigationHomeFeed(): String {
        return UriUtil.buildUriAppendParams(ApplinkConsInternalHome.HOME_NAVIGATION, mapOf(EXTRA_TAB_POSITION to TAB_POSITION_FEED))
    }

    fun getRegisteredNavigationHomeContentExplore(deeplink: String): String {
        val params = UriUtil.destructureUriToMap(ApplinkConst.CONTENT_EXPLORE, Uri.parse(deeplink), true)
        params[EXTRA_TAB_POSITION] = TAB_POSITION_FEED

        return UriUtil.buildUriAppendParams(ApplinkConsInternalHome.HOME_NAVIGATION, params.toMap())
    }

    fun getRegisteredExplore(deeplink: String): String{
        val uri = Uri.parse(deeplink)
        return when {
            uri.pathSegments.size > 0 -> ApplinkConsInternalHome.EXPLORE + uri.path
            else -> ApplinkConsInternalHome.EXPLORE
        }
    }

    fun getRegisteredInboxNavigation(deeplink: String): String {
        val useNewInbox = RemoteConfigInstance.getInstance().abTestPlatform.getString(
                AbTestPlatform.KEY_AB_INBOX_REVAMP, AbTestPlatform.VARIANT_OLD_INBOX
        ) == AbTestPlatform.VARIANT_NEW_INBOX
        val useNewNav = RemoteConfigInstance.getInstance().abTestPlatform.getString(
                AbTestPlatform.NAVIGATION_EXP_TOP_NAV, AbTestPlatform.NAVIGATION_VARIANT_OLD
        ) == AbTestPlatform.NAVIGATION_VARIANT_REVAMP
        return if (useNewInbox && useNewNav) {
            getRegisteredNavigationInbox(deeplink)
        } else {
            ApplinkConsInternalHome.HOME_INBOX
        }
    }

    private fun getRegisteredNavigationInbox(deeplink: String): String {
        var applinkInternal = ApplinkConstInternalMarketplace.INBOX
        val query = Uri.parse(deeplink).query ?: ""
        if (query.isNotEmpty()) {
            applinkInternal = "$applinkInternal?$query"
        }
        return applinkInternal
    }
}