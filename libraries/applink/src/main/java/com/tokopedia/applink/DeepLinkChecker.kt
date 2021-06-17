package com.tokopedia.applink

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.webkit.URLUtil
import com.tokopedia.applink.internal.ApplinkConstInternalCategory
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery

object DeepLinkChecker {

    private const val APP_EXCLUDED_URL = "app_excluded_url"
    private const val APP_EXCLUDED_HOST_V2 = "app_excluded_host_v2"
    private const val AMP = "amp"
    private const val EXCLUDED_AMP = "excluded_amp"
    private const val DEFAULT_EXCLUDED_AMP_VALUE = "stories"

    const val WEB_HOST = "www.tokopedia.com"
    const val MOBILE_HOST = "m.tokopedia.com"

    const val OTHER = -1
    const val BROWSE = 0
    const val HOT = 1
    const val CATALOG = 2
    const val PRODUCT = 3
    const val SHOP = 4
    const val HOT_LIST = 6
    const val CATEGORY = 7
    const val HOME = 8
    const val FIND = 9
    const val ETALASE = 10
    const val APPLINK = 11
    const val INVOICE = 12
    const val ACCOUNTS = 13
    const val RECHARGE = 14
    const val BLOG = 15
    const val DISCOVERY_PAGE = 17
    const val FLIGHT = 18
    const val REFERRAL = 19
    const val TOKOPOINT = 20
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
    const val PRODUCT_REVIEW = 35
    const val DEALS = 36
    const val TRAVEL_HOMEPAGE = 37
    const val NATIVE_THANK_YOU = 38
    const val LOGIN_BY_QR = 39

    private val deeplinkMatcher: DeeplinkMatcher by lazy { DeeplinkMatcher() }

    private fun isExcludedHostUrl(context: Context, uriData: Uri): Boolean {
        val firebaseRemoteConfig = FirebaseRemoteConfigInstance.get(context)
        val excludedHost = firebaseRemoteConfig.getString(APP_EXCLUDED_HOST_V2)
        if (excludedHost.isNullOrEmpty()) {
            return false
        }
        var host = uriData.host ?: return false
        var path = uriData.path ?: return false
        host = host.replaceFirstWww().replaceFirstM()
        path = path.replaceLastSlash()
        val uriWithoutParam = "$host$path"
        val excludedHostList = excludedHost.split(",".toRegex())
            .filter { it.isNotEmpty() }
            .map { it.replaceFirstWww().replaceFirstM().replaceLastSlash() }
        for (excludedString in excludedHostList) {
            if (uriWithoutParam.startsWith(excludedString)) {
                return true
            }
        }
        return false
    }

    private fun isExcludedUrl(context: Context, uriData: Uri): Boolean {
        val firebaseRemoteConfig = FirebaseRemoteConfigInstance.get(context)
        val excludedUrl = firebaseRemoteConfig.getString(APP_EXCLUDED_URL)
        if (excludedUrl.isNullOrEmpty()) {
            return false
        }
        var path = uriData.path ?: return false
        path = path.replaceLastSlash()
        val excludedUrlList = excludedUrl.split(",".toRegex())
            .filter { it.isNotEmpty() }
            .map { it.replaceLastSlash() }
        for (excludedString in excludedUrlList) {
            if (path.endsWith(excludedString)) {
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

    private fun String.replaceFirstM(): String {
        if (startsWith("m.")) {
            return replaceFirst("m.", "")
        }
        return this
    }

    private fun String.replaceLastSlash(): String {
        if (endsWith("/")) {
            return this.substring(0, this.length - 1)
        }
        return this
    }

    @JvmStatic
    fun getDeepLinkType(context: Context, url: String): Int {
        if (url.contains("accounts.tokopedia.com")) {
            return ACCOUNTS
        }
        if (!URLUtil.isNetworkUrl(url)) {
            return APPLINK
        }
        return try {
            val uriData = Uri.parse(url)
            if (isExcludedHostUrl(context, uriData) || isExcludedUrl(context, uriData))
                OTHER
            else if (isHome(uriData)) {
                HOME
            } else {
                deeplinkMatcher.match(uriData)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            OTHER
        }

    }

    @JvmStatic
    fun getRemoveAmpLink(context: Context, uriData: Uri): Uri? {
        val path = uriData.pathSegments
        return if (path != null && path.size > 1 && path[0] == AMP &&
                !isExcludedAmpPath(context, path[1])) {
            Uri.parse(uriData.toString().replaceFirst(AMP + "/".toRegex(), ""))
        } else uriData
    }

    @JvmStatic
    fun isAmpUrl(uriData: Uri): Boolean {
        return uriData.toString().contains("/$AMP/")
    }

    private fun isExcludedAmpPath(context: Context, path: String): Boolean {
        val firebaseRemoteConfig = FirebaseRemoteConfigInstance.get(context)
        val excludedPath = firebaseRemoteConfig.getString(EXCLUDED_AMP, defaultExcludedAmpValue())
        val excludedPathList = excludedPath.split(",".toRegex())
            .filter { it.isNotEmpty() }
            .map { it.replaceLastSlash() }
        for (excludedString in excludedPathList) {
            if (path == excludedString) {
                return true
            }
        }
        return false
    }

    private fun defaultExcludedAmpValue() = DEFAULT_EXCLUDED_AMP_VALUE

    private fun isHome(uriData: Uri): Boolean {
        return uriData.pathSegments.isEmpty() &&
            (uriData.host?.contains(WEB_HOST) ?: false || uriData.host?.contains(MOBILE_HOST) ?: false)
    }

    @JvmStatic
    fun openHot(url: String, context: Context): Boolean {
        return openIfExist(context, getHotIntent(context, url))
    }

    @JvmStatic
    fun openFind(url: String, context: Context): Boolean {
        return openIfExist(context, getFindIntent(context, url))
    }

    @JvmStatic
    fun openCatalog(url: String, context: Context): Boolean {
        return openIfExist(context, getCatalogIntent(context, url))
    }

    @JvmStatic
    fun openPromoDetail(url: String, context: Context, defaultBundle: Bundle? = null): Boolean {
        val intent = getPromoDetailIntent(context, url)
        if (defaultBundle != null) {
            intent.putExtras(defaultBundle)
        }
        return openIfExist(context, intent)
    }

    @JvmStatic
    fun openPromoList(url: String, context: Context, defaultBundle: Bundle? = null): Boolean {
        val intent = getPromoListIntent(context, url)
        if (defaultBundle != null) {
            intent.putExtras(defaultBundle)
        }
        return openIfExist(context, intent)
    }

    private fun openIfExist(context: Context, intent: Intent): Boolean {
        return if (intent.resolveActivity(context.packageManager) == null) {
            false
        } else {
            context.startActivity(intent)
            true
        }
    }

    // function for enable Hansel

    private fun getCatalogDetailClassName() = "com.tokopedia.discovery.catalogrevamp.ui.activity.CatalogDetailPageActivity"

    private fun getHotIntent(context: Context, url: String): Intent {
        val uri = Uri.parse(url)
        val query = if (uri.pathSegments.size > 1) uri.pathSegments[1] else ""
        query.replace("-","+")
        return RouteManager.getIntent(context, DeeplinkMapper.getRegisteredNavigation(context, ApplinkConst.DISCOVERY_SEARCH + "?q=" + query))
    }

    private fun getFindIntent(context: Context, url: String): Intent {
        val uri = Uri.parse(url)
        val segments = uri.pathSegments
        return RouteManager.getIntent(context, DeeplinkMapper.getRegisteredNavigation(context, ApplinkConst.FIND + "/" + if (segments.size > 1) segments[segments.lastIndex] else ""))
    }

    private fun getCatalogIntent(context: Context, url: String): Intent {
        val catalogId = getLinkSegment(url)[1]
        val intent = getIntentByClassName(context, getCatalogDetailClassName())
        intent.putExtra("EXTRA_CATALOG_ID", catalogId)
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
            intent.putExtras(bundle)
        } else {
            intent = RouteManager.getIntent(context, ApplinkConstInternalCategory.INTERNAL_CATEGORY_DETAIL, departmentId)
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
