package com.tokopedia.applink

import android.content.Context
import android.net.Uri
import android.webkit.URLUtil
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

object DeepLinkChecker {

    private val APP_EXCLUDED_URL = "app_excluded_url"
    private val APP_EXCLUDED_HOST = "app_excluded_host"

    private val WEB_HOST = "www.tokopedia.com"
    private val MOBILE_HOST = "m.tokopedia.com"

    const val OTHER = -1
    const val BROWSE = 0
    const val HOT = 1
    const val CATALOG = 2
    const val PRODUCT = 3
    const val SHOP = 4
    const val TOPPICKS = 5
    const val HOT_LIST = 6
    const val CATEGORY = 7
    const val HOME = 8
    const val PROMO = 9
    const val ETALASE = 10
    const val APPLINK = 11
    const val INVOICE = 12
    const val ACCOUNTS = 13
    const val RECHARGE = 14
    const val BLOG = 15
    const val PELUANG = 16
    const val DISCOVERY_PAGE = 17
    const val FLIGHT = 18
    const val REFERRAL = 19
    const val TOKOPOINT = 20
    const val GROUPCHAT = 21
    const val SALE = 22
    const val WALLET_OVO = 23
    const val PLAY = 24
    const val PROFILE = 25
    const val CONTENT = 26
    const val SMCREFERRAL = 27
    const val RECOMMENDATION = 28
    const val ORDER_LIST = 29


    private val deeplinkMatcher: DeeplinkMatcher by lazy { DeeplinkMatcher() }

    private fun isExcludedHostUrl(context: Context, uriData: Uri): Boolean {
        val firebaseRemoteConfig = FirebaseRemoteConfigImpl(context)
        val excludedHost = firebaseRemoteConfig.getString(APP_EXCLUDED_HOST)
        if (excludedHost.isNullOrEmpty()) {
            return false;
        }
        val scheme = uriData.scheme ?: return false
        val host = uriData.host ?: return false
        val path = uriData.path ?: return false
        val uriWithoutParam = scheme + host + path
        val excludedHostList = excludedHost.split(",".toRegex()).dropLastWhile { it.isEmpty() }
        for (excludedString in excludedHostList) {
            if (uriWithoutParam.startsWith(excludedString)) {
                return true
            }
        }
        return false
    }

    private fun isExcludedUrl(context: Context, uriData: Uri): Boolean {
        val firebaseRemoteConfig = FirebaseRemoteConfigImpl(context)
        val excludedUrl = firebaseRemoteConfig.getString(APP_EXCLUDED_URL)
        if (excludedUrl.isNullOrEmpty()) {
            return false;
        }
        val path = uriData.path ?: return false
        val excludedUrlList = excludedUrl.split(",".toRegex()).dropLastWhile { it.isEmpty() }
        for (excludedString in excludedUrlList) {
            if (path.endsWith(excludedString)) {
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun getDeepLinkType(context: Context, url: String): Int {
        if (url.contains("accounts.tokopedia.com")) {
            return ACCOUNTS
        }
        if (!URLUtil.isNetworkUrl(url)) {
            return APPLINK
        }
        try {
            val uriData = Uri.parse(url)
            return if (isExcludedHostUrl(context, uriData) || isExcludedUrl(context, uriData))
                OTHER
            else if (isHome(uriData)) {
                HOME
            } else {
                deeplinkMatcher.match(uriData)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return OTHER
        }

    }

    private fun isHome(uriData: Uri): Boolean {
        return uriData.pathSegments.isEmpty() &&
            (uriData.host?.contains(WEB_HOST) ?: false || uriData.host?.contains(MOBILE_HOST) ?: false)
    }

}
