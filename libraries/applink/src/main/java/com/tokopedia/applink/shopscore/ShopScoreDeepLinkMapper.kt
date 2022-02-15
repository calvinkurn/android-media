package com.tokopedia.applink.shopscore

import android.net.Uri
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace

object ShopScoreDeepLinkMapper {

    const val COACH_MARK_PARAM = "coachmark"

    fun getInternalAppLinkShopScore(appLink: String): String {
        val uri = Uri.parse(appLink)
        val coachMark = uri.getQueryParameter(appLink).orEmpty()
        return if (coachMark.isEmpty()) {
            ApplinkConstInternalMarketplace.SHOP_PERFORMANCE
        } else {
            val params = mapOf<String, Any>(COACH_MARK_PARAM to coachMark)
            UriUtil.buildUriAppendParams(ApplinkConstInternalMarketplace.SHOP_PERFORMANCE, params)
        }
    }
}