package com.tokopedia.applink.marketplace

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.config.GlobalConfig

/**
 * Created by Irfan Khoirul on 2019-10-08.
 */

object DeeplinkMapperMarketplace {

    fun getRegisteredNavigationMarketplace(deeplink: String): String {
        val uri = Uri.parse(deeplink)
        return when {
            deeplink.startsWith(ApplinkConst.CART) ->
                ApplinkConstInternalMarketplace.CART
            deeplink.startsWith(ApplinkConst.CHECKOUT) ->
                deeplink.replace(ApplinkConst.CHECKOUT, ApplinkConstInternalMarketplace.CHECKOUT)
            deeplink.startsWith(ApplinkConst.GOLD_MERCHANT_STATISTIC_DASHBOARD) ->
                deeplink.replace(ApplinkConst.GOLD_MERCHANT_STATISTIC_DASHBOARD, ApplinkConstInternalMarketplace.GOLD_MERCHANT_STATISTIC_DASHBOARD)
            deeplink.startsWith(ApplinkConst.SHOP_SCORE_DETAIL) ->
                deeplink.replace(ApplinkConst.SHOP_SCORE_DETAIL, ApplinkConstInternalMarketplace.SHOP_SCORE_DETAIL)
            deeplink.startsWith(ApplinkConst.PRODUCT_ADD) -> {
                if (GlobalConfig.isSellerApp()) {
                    ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW
                } else {
                    ApplinkConstInternalMarketplace.PRODUCT_ADD_ITEM
                }
            }
            deeplink.startsWith(ApplinkConst.OCC) ->
                deeplink.replace(ApplinkConst.OCC, ApplinkConstInternalMarketplace.ONE_CLICK_CHECKOUT)
            else -> return deeplink
        }
    }

}