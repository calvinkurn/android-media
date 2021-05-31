package com.tokopedia.applink.marketplace

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkConst.ADD_PATH
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalTokoMart
import com.tokopedia.applink.salam.DeeplinkMapperSalam
import com.tokopedia.applink.shopscore.DeepLinkMapperShopScore
import com.tokopedia.applink.statistic.DeepLinkMapperStatistic

/**
 * Created by Irfan Khoirul on 2019-10-08.
 */

object DeeplinkMapperMarketplace {

    fun getRegisteredNavigationMarketplace(context: Context, deeplink: String): String {
        val uri = Uri.parse(deeplink)
        return when {
            deeplink.startsWith(ApplinkConst.CART) ->
                ApplinkConstInternalMarketplace.CART
            deeplink.startsWith(ApplinkConst.CHECKOUT) ->
                deeplink.replace(ApplinkConst.CHECKOUT, ApplinkConstInternalMarketplace.CHECKOUT)
            deeplink.startsWith(ApplinkConst.GOLD_MERCHANT_STATISTIC_DASHBOARD) -> DeepLinkMapperStatistic.getStatisticAppLink(uri)
            deeplink.startsWith(ApplinkConst.SHOP_SCORE_DETAIL) -> DeepLinkMapperShopScore.getShopScoreApplink(context, deeplink)
            deeplink.startsWith(ApplinkConst.PRODUCT_ADD) -> ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW
            deeplink.startsWith(ApplinkConst.OCC) ->
                deeplink.replace(ApplinkConst.OCC, ApplinkConstInternalMarketplace.ONE_CLICK_CHECKOUT)
            else -> return deeplink
        }
    }

    fun getTokopediaInternalProduct(uri:Uri, idList: List<String>?):String {
        return if (uri.pathSegments[0] == ADD_PATH) {
            ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW
        } else {
            UriUtil.buildUri(ApplinkConstInternalMarketplace.PRODUCT_DETAIL, idList?.getOrNull(0))
        }
    }

    fun getShopPageInternalAppLink(ctx: Context, uri: Uri, deeplink: String, internalAppLink: String, shopId: String):String {
        return if (isSpecialShop(shopId) && uri.pathSegments.size == 1) {
            DeeplinkMapperSalam.getRegisteredNavigationSalamUmrahShop(deeplink, ctx)
        } else if(isTokoNowShopId(shopId)){
            ApplinkConstInternalTokoMart.HOME
        } else {
            internalAppLink
        }
    }

    private fun isSpecialShop(shopId: String): Boolean {
        return shopId == ApplinkConst.SALAM_UMRAH_SHOP_ID
    }

    private fun isTokoNowShopId(shopId: String): Boolean {
        return shopId == ApplinkConst.TokoNow.TOKONOW_PRODUCTION_SHOP_ID
    }

}