package com.tokopedia.applink.merchant

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace

/**
 * Created by Rafli Syam on 2020-02-04.
 */

object DeeplinkMapperMerchant {

    private const val PARAM_RATING = "rating"
    private const val PARAM_UTM_SOURCE = "utm_source"

    fun getRegisteredNavigationReputation(deeplink: String): String {
        if(deeplink.startsWith(ApplinkConst.REPUTATION)) {
            val parsedUri = Uri.parse(deeplink)
            val segments = parsedUri.pathSegments
            if(segments.size > 0) {
                val reputationId = segments.last()
                val newUri = UriUtil.buildUri(ApplinkConstInternalMarketplace.INBOX_REPUTATION_DETAIL, reputationId)
                return Uri.parse(newUri)
                        .buildUpon()
                        .build()
                        .toString()
            }
            return ApplinkConstInternalMarketplace.INBOX_REPUTATION
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