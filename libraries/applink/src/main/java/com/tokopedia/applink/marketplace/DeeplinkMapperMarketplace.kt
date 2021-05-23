package com.tokopedia.applink.marketplace

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkConst.ADD_PATH
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.salam.DeeplinkMapperSalam
import com.tokopedia.applink.statistic.DeepLinkMapperStatistic

/**
 * Created by Irfan Khoirul on 2019-10-08.
 */

object DeeplinkMapperMarketplace {

    fun getRegisteredNavigationMarketplace(deeplink: String): String {
        val uri = Uri.parse(deeplink)
        return when {
            deeplink.startsWith(ApplinkConst.MINI_CART_EXAMPLE) ->
                ApplinkConstInternalMarketplace.MINI_CART_EXAMPLE
            deeplink.startsWith(ApplinkConst.CART) ->
                ApplinkConstInternalMarketplace.CART
            deeplink.startsWith(ApplinkConst.CHECKOUT) ->
                deeplink.replace(ApplinkConst.CHECKOUT, ApplinkConstInternalMarketplace.CHECKOUT)
            deeplink.startsWith(ApplinkConst.GOLD_MERCHANT_STATISTIC_DASHBOARD) -> DeepLinkMapperStatistic.getStatisticAppLink(uri)
            deeplink.startsWith(ApplinkConst.SHOP_SCORE_DETAIL) ->
                deeplink.replace(ApplinkConst.SHOP_SCORE_DETAIL, ApplinkConstInternalMarketplace.SHOP_SCORE_DETAIL)
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

    fun getInternalShopPage(ctx: Context, uri: Uri, deeplink: String, idList:List<String>?):String {
        return if (isSpecialShop(uri)) {
            DeeplinkMapperSalam.getRegisteredNavigationSalamUmrahShop(deeplink, ctx)
        } else {
            UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE, idList?.getOrNull(0))
        }
    }

    private fun isSpecialShop(uri: Uri): Boolean {
        return (uri.pathSegments[0] == ApplinkConst.SALAM_UMRAH_SHOP_ID)
    }
}