package com.tokopedia.applink

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.webkit.URLUtil
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalOrderDetail
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

object DeepLinkChecker {

    private val APP_EXCLUDED_URL = "app_excluded_url"
    private val APP_EXCLUDED_HOST_V2 = "app_excluded_host_v2"

    @JvmField
    val WEB_HOST = "www.tokopedia.com"
    @JvmField
    val MOBILE_HOST = "m.tokopedia.com"

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
    const val CONTACT_US = 30
    const val HOTEL = 31
    const val SIMILAR_PRODUCT = 32
    const val PROMO_DETAIL = 33
    const val PROMO_LIST = 34

    private val deeplinkMatcher: DeeplinkMatcher by lazy { DeeplinkMatcher() }

    private fun isExcludedHostUrl(context: Context, uriData: Uri): Boolean {
        val firebaseRemoteConfig = FirebaseRemoteConfigImpl(context)
        val excludedHost = firebaseRemoteConfig.getString(APP_EXCLUDED_HOST_V2)
        if (excludedHost.isNullOrEmpty()) {
            return false
        }
        var host = uriData.host ?: return false
        val path = uriData.path ?: return false
        host = host.replaceFirstWww()
        val uriWithoutParam = "$host$path"
        val excludedHostList = excludedHost.split(",".toRegex())
            .filter { it.isNotEmpty() }
            .map { it.replaceFirstWww() }
        for (excludedString in excludedHostList) {
            if (uriWithoutParam.startsWith(excludedString)) {
                return true
            }
        }
        return false
    }

    private fun String.replaceFirstWww(): String {
        if (startsWith("www.")) {
            return replaceFirst("www.", "")
        }
        return this
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

    /**
     * will check if the url match with the native page, do the map, and launch the activity.
     * @return true if it is successfully find the matching native page and launch the activity
     * Example: https://www.tokopedia.com will launch MainParentActivity and will return true.
     */
    @JvmStatic
    fun moveToNativePageFromWebView(activity: Activity, url: String): Boolean {
        if (url.endsWith(".pl")) {
            return false
        }
        if (!url.contains(WEB_HOST) && !url.contains(MOBILE_HOST)) {
            return false
        }
        val deeplinkType = getDeepLinkType(activity, url)
        when (deeplinkType) {
            HOME -> {
                val intent = RouteManager.getIntentNoFallback(activity, ApplinkConst.HOME)
                if (intent != null) {
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
                    activity.startActivity(intent)
                }
                return true
            }
            CATEGORY -> {
                val departmentId = getLinkSegment(url)[1]
                RouteManager.route(activity, ApplinkConstInternalMarketplace.DISCOVERY_CATEGORY_DETAIL, departmentId)
                return true
            }
            BROWSE -> {
                openBrowse(url, activity)
                return true
            }
            HOT -> {
                //TODO still use className
                return openHot(url, activity)
            }
            CATALOG -> {
                //TODO still use className
                return openCatalog(url, activity)
            }
            PRODUCT -> {
                RouteManager.route(activity, url)
                return true
            }
            TOKOPOINT -> {
                // it still point to webview. no need to override
                return false
            }
            WALLET_OVO -> {
                // it still point to webview. no need to override
                return false
            }
            PROFILE -> {
                val userId = getLinkSegment(url)[1]
                return RouteManager.route(activity, ApplinkConst.PROFILE, userId)
            }
            CONTENT -> {
                val contentId = getLinkSegment(url)[1]
                return RouteManager.route(activity, ApplinkConst.PROFILE, contentId)
            }
            HOTEL -> {
                return RouteManager.route(activity, url)
            }
            ORDER_LIST -> {
                RouteManager.route(activity, ApplinkConstInternalOrderDetail.ORDER_LIST_URL, url)
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun openHot(url: String, context: Context): Boolean {
        return openIfExist(context, getHotIntent(context, url))
    }

    @JvmStatic
    fun openCatalog(url: String, context: Context): Boolean {
        return openIfExist(context, getCatalogIntent(context, url))
    }

    @JvmStatic
    fun openPromoDetail(url: String, context: Context): Boolean {
        return openIfExist(context, getPromoDetailIntent(context, url))
    }

    @JvmStatic
    fun openPromoList(url: String, context: Context): Boolean {
        return openIfExist(context, getPromoListIntent(context, url))
    }

    private fun openIfExist(context: Context, intent: Intent): Boolean {
        if (intent.resolveActivity(context.packageManager) == null) {
            return false
        } else {
            context.startActivity(intent)
            return true
        }
    }

    // function for enable Hansel
    private fun getHotListClassName() = "com.tokopedia.discovery.newdiscovery.hotlist.view.activity.HotlistActivity"

    private fun getCatalogDetailClassName() = "com.tokopedia.discovery.catalog.activity.CatalogDetailActivity"

    private fun getHotIntent(context: Context, url: String): Intent {
        val intent = getIntentByClassName(context, getHotListClassName())
        intent.putExtra("HOTLIST_URL", url)
        return intent
    }

    private fun getCatalogIntent(context: Context, url: String): Intent {
        val catalogId = getLinkSegment(url)[1]
        val intent = getIntentByClassName(context, getCatalogDetailClassName())
        intent.putExtra("ARG_EXTRA_CATALOG_ID", catalogId)
        return intent
    }

    private fun getPromoDetailIntent(context: Context, url: String): Intent {
        val promoSlug = getLinkSegment(url)[1]
        return RouteManager.getIntent(context, ApplinkConst.PROMO_DETAIL, promoSlug)
    }

    private fun getPromoListIntent(context: Context, url: String): Intent {
        return RouteManager.getIntent(context, ApplinkConst.PROMO_LIST)
    }

    @JvmStatic
    fun openBrowse(url: String, context: Context): Boolean {
        val uriData = Uri.parse(url)
        val bundle = Bundle()

        val departmentId = uriData.getQueryParameter("sc")
        bundle.putBoolean("IS_DEEP_LINK_SEARCH", true)
        val intent: Intent
        if (departmentId.isNullOrEmpty()) {
            intent = RouteManager.getIntent(context, constructSearchApplink(uriData))
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtras(bundle)
        } else {
            intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.DISCOVERY_CATEGORY_DETAIL, departmentId)
        }
        return openIfExist(context, intent)
    }

    private fun constructSearchApplink(uriData: Uri): String {
        val q = uriData.getQueryParameter("q")

        val applink = if (TextUtils.isEmpty(q))
            ApplinkConstInternalDiscovery.AUTOCOMPLETE
        else
            ApplinkConstInternalDiscovery.SEARCH_RESULT

        return applink + "?" + uriData.query
    }

    private fun getIntentByClassName(context: Context, className: String): Intent {
        return Intent().apply {
            setClassName(context.packageName, className)
        }
    }

    private fun getLinkSegment(url: String): List<String> {
        return Uri.parse(url).pathSegments
    }

}
