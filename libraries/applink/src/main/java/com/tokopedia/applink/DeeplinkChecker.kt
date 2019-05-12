package com.tokopedia.applink

import android.content.Context
import android.net.Uri
import android.webkit.URLUtil
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

object DeeplinkChecker {

    private val APP_EXCLUDED_URL = "app_excluded_url"
    private val APP_EXCLUDED_HOST = "app_excluded_host"

    private val WEB_HOST = "www.tokopedia.com"
    private val MOBILE_HOST = "m.tokopedia.com"

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
            return DeeplinkMatcher.ACCOUNTS
        }
        if (!URLUtil.isNetworkUrl(url)) {
            return DeeplinkMatcher.APPLINK
        }
        try {
            val uriData = Uri.parse(url)
            return if (isExcludedHostUrl(context, uriData) || isExcludedUrl(context, uriData))
                DeeplinkMatcher.OTHER
            else if (isHome(uriData)) {
                DeeplinkMatcher.HOME
            } else {
                deeplinkMatcher.match(uriData)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return DeeplinkMatcher.OTHER
        }

    }

    private fun isHome(uriData: Uri): Boolean {
        return uriData.pathSegments.isEmpty() &&
            (uriData.host?.contains(WEB_HOST) ?: false || uriData.host?.contains(MOBILE_HOST) ?: false)
    }

}
