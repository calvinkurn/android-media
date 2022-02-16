package com.tokopedia.applink.shopscore

import android.net.Uri
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace

object ShopScoreDeepLinkMapper {

    const val COACH_MARK_PARAM = "coachmark"

    /**
     * mapping applink external to internal
     * e.g, external
     * MA with param: tokopedia://shop-score-detail?coachmark=disabled
     * MA without param: tokopedia://shop-score-detail
     * SA with param: sellerapp://shop-score-detail?coachmark=disabled
     * SA without param: sellerapp://shop-score-detail
     * to Internal Applink
     * with param: tokopedia-android-internal://marketplace/shop/performance?coachmark=disabled
     * without param: tokopedia-android-internal://marketplace/shop/performance
     */
    fun getInternalAppLinkShopScore(uri: Uri): String {
        val coachMark = uri.getQueryParameter(COACH_MARK_PARAM).orEmpty()
        return if (coachMark.isEmpty()) {
            ApplinkConstInternalMarketplace.SHOP_PERFORMANCE
        } else {
            val params = mapOf<String, Any>(COACH_MARK_PARAM to coachMark)
            UriUtil.buildUriAppendParams(ApplinkConstInternalMarketplace.SHOP_PERFORMANCE, params)
        }
    }
}