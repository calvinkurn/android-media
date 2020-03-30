package com.tokopedia.applink.merchant

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.startsWithPattern

/**
 * Created by Rafli Syam on 2020-02-04.
 */

object DeeplinkMapperMerchant {

    private const val PARAM_RATING = "rating"
    private const val PARAM_UTM_SOURCE = "utm_source"
    private const val DEFAULT_SHOP_ID = "0"
    private const val ACTION_REVIEW = "review"
    private const val SHOP_PAGE_SEGMENT_SIZE = 1
    private const val SHOP_REVIEW_SEGMENT_SIZE = 2

    fun getRegisteredNavigationReputation(deeplink: String): String {
        if(deeplink.startsWith(ApplinkConst.REPUTATION)) {
            val parsedUri = Uri.parse(deeplink)
            val segments = parsedUri.pathSegments
            if(segments.size > 0) {
                val reputationId = segments.last()
                return UriUtil.buildUri(ApplinkConstInternalMarketplace.INBOX_REPUTATION_DETAIL, reputationId)
            }
            return ApplinkConstInternalMarketplace.INBOX_REPUTATION
        }
        return deeplink
    }

    fun isShopPage(deeplink: String): Boolean {
        val uri = Uri.parse(deeplink)
        return deeplink.startsWithPattern(ApplinkConst.SHOP) && uri.pathSegments.size == SHOP_PAGE_SEGMENT_SIZE
    }

    fun isShopReview(deeplink: String): Boolean {
        val uri = Uri.parse(deeplink)
        return deeplink.startsWithPattern(ApplinkConst.SHOP_REVIEW) && uri.lastPathSegment == ACTION_REVIEW
    }

    fun getRegisteredNavigationShopPage(deeplink: String): String {
        if(deeplink.startsWithPattern(ApplinkConst.SHOP)) {
            val segments = Uri.parse(deeplink).pathSegments
            if (segments.size > 0) {
                val shopId = segments[0]
                return UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE, shopId)
            }
        }
        return deeplink
    }

    fun getRegisteredNavigationShopReview(deeplink: String): String {
        if(deeplink.startsWithPattern(ApplinkConst.SHOP_REVIEW)) {
            val segments = Uri.parse(deeplink).pathSegments
            val shopId = segments[0]
            return if(segments.size == SHOP_REVIEW_SEGMENT_SIZE) {
                UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_REVIEW_APPLINK, shopId)
            } else {
                UriUtil.buildUri(deeplink.replace(ApplinkConst.SHOP, ApplinkConstInternalMarketplace.SHOP_PAGE), shopId)
            }
        }
        return deeplink
    }

    fun getRegisteredNavigationProductReview(deeplink: String): String {
        if(deeplink.startsWith(ApplinkConst.PRODUCT_CREATE_REVIEW)) {
            val parsedUri = Uri.parse(deeplink)
            val segments = parsedUri.pathSegments
            val rating = parsedUri.getQueryParameter(PARAM_RATING) ?: "5"
            val utmSource = parsedUri.getQueryParameter(PARAM_UTM_SOURCE) ?: ""

            val reputationId = segments[segments.size - 2]
            val productId = segments.last()
            val newUri = UriUtil.buildUri(ApplinkConstInternalMarketplace.CREATE_REVIEW, reputationId, productId)
            return Uri.parse(newUri)
                    .buildUpon()
                    .appendQueryParameter(PARAM_RATING, rating)
                    .appendQueryParameter(PARAM_UTM_SOURCE, utmSource)
                    .build()
                    .toString()
        }
        return deeplink
    }

    fun isShopPageDeeplink(deeplink: String): Boolean {
        val uri = Uri.parse(deeplink) ?: return false
        val pathSegment = uri.pathSegments ?: return false
        val prefixShopPageApplink = "tokopedia://shop/"
        return deeplink.startsWith(prefixShopPageApplink).and(pathSegment.size == 1)
    }

    fun getShopPageInternalApplink(deeplink: String): String {
        val uri = Uri.parse(deeplink) ?: return deeplink
        val pathSegment = uri.pathSegments ?: return deeplink
        return if (pathSegment.size == 1) {
            val shopId = pathSegment[0]
            UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE, shopId)
        }
        else ""
    }

    fun isShopPageHomeDeeplink(deeplink: String): Boolean {
        val uri = Uri.parse(deeplink) ?: return false
        val pathSegment = uri.pathSegments ?: return false
        val prefixShopPageHomeAppLink = "tokopedia://shop/"
        val pathHome = "home"
        return if (pathSegment.size == 2)
            deeplink.startsWith(prefixShopPageHomeAppLink) and (pathSegment[1] == pathHome)
        else false
    }

    fun getShopPageHomeInternalApplink(deeplink: String): String {
        val uri = Uri.parse(deeplink) ?: return deeplink
        val pathSegment = uri.pathSegments ?: return deeplink
        return if (pathSegment.size == 2) {
            val shopId = pathSegment[0]
            UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE_HOME, shopId)
        }
        else deeplink
    }

    fun isShopPageInfoDeeplink(deeplink: String): Boolean {
        val uri = Uri.parse(deeplink) ?: return false
        val pathSegment = uri.pathSegments ?: return false
        val prefixShopPageHomeAppLink = "tokopedia://shop/"
        val pathInfo = "info"
        return if (pathSegment.size == 2)
            deeplink.startsWith(prefixShopPageHomeAppLink) and (pathSegment[1] == pathInfo)
        else false
    }

    fun isShopPageNoteDeeplink(deeplink: String): Boolean {
        val uri = Uri.parse(deeplink) ?: return false
        val pathSegment = uri.pathSegments ?: return false
        val prefixShopPageHomeAppLink = "tokopedia://shop/"
        val pathNote = "note"
        return if (pathSegment.size == 2)
            deeplink.startsWith(prefixShopPageHomeAppLink) and (pathSegment[1] == pathNote)
        else false
    }

    fun getShopPageInfoInternalApplink(deeplink: String): String {
        val uri = Uri.parse(deeplink) ?: return deeplink
        val pathSegment = uri.pathSegments ?: return deeplink
        return if (pathSegment.size == 2) {
            val shopId = pathSegment[0]
            UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE_INFO, shopId)
        }
        else deeplink
    }

    fun isShopPageResultEtalaseDeepLink(deeplink: String): Boolean {
        val uri = Uri.parse(deeplink) ?: return false
        val pathSegment = uri.pathSegments ?: return false
        val prefixShopPageHomeAppLink = "tokopedia://shop/"
        val pathEtalase = "etalase"
        return if (pathSegment.size == 3)
            deeplink.startsWith(prefixShopPageHomeAppLink) and (pathSegment[1] == pathEtalase)
        else false
    }

    fun getShopPageResultEtalaseInternalAppLink(deeplink: String): String {
        val uri = Uri.parse(deeplink) ?: return deeplink
        val pathSegment = uri.pathSegments ?: return deeplink
        return if (pathSegment.size == 3) {
            val shopId = pathSegment[0]
            val etalaseId = pathSegment[2]
            UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE_PRODUCT_LIST, shopId, etalaseId)
        }
        else deeplink
    }

}