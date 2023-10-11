package com.tokopedia.applink.shopscore

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.FirebaseRemoteConfigInstance
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.remoteconfig.RemoteConfigKey

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

    fun getInternalApplinkPenalty(context: Context): String {
        return if (getIsShopScorePenaltyNew(context)) {
            ApplinkConstInternalMarketplace.SHOP_PENALTY
        } else {
            ApplinkConstInternalMarketplace.SHOP_PENALTY_OLD
        }
    }

    private fun getIsShopScorePenaltyNew(context: Context): Boolean {
        return FirebaseRemoteConfigInstance.get(context).getBoolean(RemoteConfigKey.IS_SHOP_PENALTY_NEW_PAGE)
            .orFalse()
    }
}
