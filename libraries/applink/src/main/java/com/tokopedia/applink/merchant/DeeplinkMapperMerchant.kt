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
    private const val APPLINK_SHOP_INFO_SEGMENT_SIZE = 2
    private const val DEFAULT_SHOP_INFO_SEGMENT_SIZE = 1

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

    fun getRegisteredNavigationShopReview(deeplink: String): String {
        if(deeplink.startsWithPattern(ApplinkConst.SHOP_REVIEW) || deeplink.startsWithPattern(ApplinkConst.SHOP)) {
            val segments = Uri.parse(deeplink).pathSegments
            val shopId: String
            return when(segments.size) {
                DEFAULT_SHOP_INFO_SEGMENT_SIZE -> {
                    shopId = segments[segments.size - 1]
                    UriUtil.buildUri(deeplink.replace(ApplinkConst.SHOP, ApplinkConstInternalMarketplace.SHOP_PAGE), shopId)
                }
                APPLINK_SHOP_INFO_SEGMENT_SIZE -> {
                    shopId = segments[segments.size - 2]
                    UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_REVIEW_APPLINK, shopId)
                }
                else -> {
                    UriUtil.buildUri(deeplink.replace(ApplinkConst.SHOP, ApplinkConstInternalMarketplace.SHOP_PAGE), DEFAULT_SHOP_ID)
                }
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

}