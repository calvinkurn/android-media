package com.tokopedia.applink

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import com.tokopedia.applink.internal.ApplinkConstInternalCategory
import com.tokopedia.applink.order.DeeplinkMapperOrder
import java.lang.Exception

/**
 * will check if the url match with the native page, do the map, and launch the activity.
 * @return true if it is successfully find the matching native page and launch the activity
 * Example: https://www.tokopedia.com will launch MainParentActivity and will return true.
 */
object RouteManagerKt {
    /**
     * Route http url to native page if possible.
     * Will return true for successful routing.
     * Example: http://www.tokopedia.com/pulsa to native page
     */
    @JvmStatic
    fun moveToNativePageFromWebView(activity: Activity, url: String): Boolean {
        if (url.endsWith(".pl")) {
            return false
        }
        if (!url.contains(DeepLinkChecker.WEB_HOST) && !url.contains(DeepLinkChecker.MOBILE_HOST) &&
            !UriUtil.isHostStaging(url) && !url.contains(DeepLinkChecker.WEB_HOST_SELLER)
        ) {
            return false
        }
        val registeredNavigation = DeeplinkMapper.getRegisteredNavigationFromHttp(
            activity.applicationContext,
            Uri.parse(url),
            url
        )
        if (!TextUtils.isEmpty(registeredNavigation)) {
            val intent = RouteManager.getIntentNoFallback(activity, registeredNavigation)
            if (intent != null) {
                activity.startActivity(intent)
                return true
            }
            return true
        }
        when (DeepLinkChecker.getDeepLinkType(activity, url)) {
            DeepLinkChecker.HOME -> {
                val intent = RouteManager.getIntentNoFallback(activity, ApplinkConst.HOME)
                if (intent != null) {
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
                    activity.startActivity(intent)
                }
                return true
            }
            DeepLinkChecker.CATEGORY -> {
                val departmentId = getLinkSegment(url)[1]
                RouteManager.route(
                    activity,
                    ApplinkConstInternalCategory.INTERNAL_CATEGORY_DETAIL,
                    departmentId
                )
                return true
            }
            DeepLinkChecker.BROWSE -> {
                DeepLinkChecker.openBrowse(url, activity)
                return true
            }
            DeepLinkChecker.HOT -> {
                return DeepLinkChecker.openHot(url, activity)
            }
            DeepLinkChecker.CATALOG -> {
                // TODO still use className
                return DeepLinkChecker.openCatalog(url, activity)
            }
            DeepLinkChecker.TOKOPOINT -> {
                // it still point to webview. no need to override
                return false
            }
            DeepLinkChecker.WALLET_OVO -> {
                // it still point to webview. no need to override
                return false
            }
            DeepLinkChecker.PROFILE -> {
                val userId = getLinkSegment(url)[1]
                return RouteManager.route(activity, ApplinkConst.PROFILE, userId)
            }
            DeepLinkChecker.CONTENT -> {
                val contentId = getLinkSegment(url)[1]
                return RouteManager.route(activity, ApplinkConst.CONTENT_DETAIL, contentId)
            }
            DeepLinkChecker.HOTEL -> {
                return RouteManager.route(activity, url)
            }
            DeepLinkChecker.ORDER_LIST -> {
                val intent =
                    RouteManager.getIntentNoFallback(activity, ApplinkConst.MARKETPLACE_ORDER)
                if (intent != null) {
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
                    activity.startActivity(intent)
                }
                return true
            }
            DeepLinkChecker.TRAVEL_HOMEPAGE -> {
                return RouteManager.route(activity, url)
            }
            DeepLinkChecker.NATIVE_THANK_YOU -> {
                val merchantCode = getLinkSegment(url)[2]
                val paymentId = getLinkSegment(url)[3]
                return RouteManager.route(
                    activity,
                    ApplinkConst.THANKYOU_PAGE_NATIVE,
                    paymentId,
                    merchantCode
                )
            }
            DeepLinkChecker.SALDO_DEPOSIT -> {
                return RouteManager.route(activity, ApplinkConst.SALDO)
            }
            DeepLinkChecker.SNAPSHOT -> {
                val snapShotOrderAppLink = DeeplinkMapperOrder.mapSnapShotAppLinkFromHttp(url)
                return RouteManager.route(activity, snapShotOrderAppLink)
            }
        }
        return false
    }

    private fun getLinkSegment(url: String): List<String> {
        return Uri.parse(url).pathSegments
    }

    /**
     * Method to fix backend wrong double pattern
     * Example:
     * tokopedia://tokopedia://tokopedia://discovery/clp_otomotif_63
     * tokopedia://tokopedia://discovery/clp_fashion-pria_1759
     * tokopedia://tokopedia://discovery/clp_fashion-wanita_1758
     * AN-33822
     */
    @JvmStatic
    fun trimDoubleSchemeDeeplink(url: String): String {
        var urlTrimmed = url
        val scheme = urlTrimmed.substringBefore("://")
        while (hasDoubleScheme(urlTrimmed, scheme)) {
            urlTrimmed = urlTrimmed.replace("$scheme://$scheme://", "$scheme://")
        }
        return urlTrimmed
    }

    private fun hasDoubleScheme(url: String, scheme: String?): Boolean {
        return try {
            val doubleScheme = "$scheme://$scheme://"
            url.startsWith(doubleScheme)
        } catch (e: Exception) {
            false
        }
    }
}
