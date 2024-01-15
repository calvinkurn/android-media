package com.tokopedia.applink.marketplace

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkConst.ADD_PATH
import com.tokopedia.applink.ApplinkConst.AFFILIATE_UNIQUE_ID
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.applink.startsWithPattern
import com.tokopedia.applink.statistic.DeepLinkMapperStatistic
import com.tokopedia.config.GlobalConfig
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl

/**
 * Created by Irfan Khoirul on 2019-10-08.
 */

object DeeplinkMapperMarketplace {

    private const val PARAM_SOURCE = "review-source"
    private const val REVIEW_FULL_PAGE_SOURCE = "header"
    private const val ACTION_REVIEW = "review"

    fun getRegisteredNavigationMarketplace(deeplink: String): String {
        val uri = Uri.parse(deeplink)
        return when {
            deeplink.startsWith(ApplinkConst.CART) ->
                ApplinkConstInternalMarketplace.CART
            deeplink.startsWith(ApplinkConst.CHECKOUT) ->
                deeplink.replace(ApplinkConst.CHECKOUT, ApplinkConstInternalMarketplace.CHECKOUT)
            deeplink.startsWith(ApplinkConst.GOLD_MERCHANT_STATISTIC_DASHBOARD) -> DeepLinkMapperStatistic.getStatisticAppLink(uri)
            deeplink.startsWith(ApplinkConst.PRODUCT_ADD) -> ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW
            deeplink.startsWith(ApplinkConst.OCC) ->
                deeplink.replace(ApplinkConst.OCC, ApplinkConstInternalMarketplace.ONE_CLICK_CHECKOUT)
            else -> return deeplink
        }
    }

    fun getTokopediaInternalProduct(uri: Uri, idList: List<String>?): String {
        return if (uri.pathSegments[0] == ADD_PATH) {
            ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW
        } else if (uri.queryParameterNames.contains(AFFILIATE_UNIQUE_ID)) {
            UriUtil.buildUri(ApplinkConstInternalMarketplace.PRODUCT_DETAIL_WITH_AFFILIATE_UUID, idList?.getOrNull(0), uri.getQueryParameter("aff_unique_id"))
        } else {
            UriUtil.buildUri(ApplinkConstInternalMarketplace.PRODUCT_DETAIL, idList?.getOrNull(0))
        }
    }

    fun getShopPageInternalAppLink(ctx: Context, uri: Uri, deeplink: String, internalAppLink: String, shopId: String): String {
        return if (isTokopediaNowShopId(shopId) && !GlobalConfig.isSellerApp()) {
            ApplinkConstInternalTokopediaNow.HOME
        } else {
            if (isShopReviewAppLink(deeplink)) {
                getShopReviewDestinationPage(uri, shopId)
            } else {
                internalAppLink
            }
        }
    }
    fun getShopOperationalHourInternalAppLink(shopId: String): String {
        return UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_OPERATIONAL_HOUR_BOTTOM_SHEET, shopId)
    }

    fun getShopMvcLockedToProductShopIdInternalAppLink(shopId: String, voucherId: String): String {
        return UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_MVC_LOCKED_TO_PRODUCT, shopId, voucherId)
    }

    private fun isShopReviewAppLink(deeplink: String): Boolean {
        return Uri.parse(deeplink).let {
            deeplink.startsWithPattern(ApplinkConst.SHOP_REVIEW) && it.lastPathSegment == ACTION_REVIEW
        }
    }

    fun getShopReviewDestinationPage(uri: Uri, shopId: String): String {
        val source = uri.getQueryParameter(PARAM_SOURCE).orEmpty()
        return if (source.isNotEmpty() && source == REVIEW_FULL_PAGE_SOURCE) {
            // review page full page
            UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_REVIEW_FULL_PAGE, shopId)
        } else {
            // go to shop page and redirect to review tab
            UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE_REVIEW, shopId)
        }
    }

    private fun isTokopediaNowShopId(shopId: String): Boolean {
        return if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
            shopId == ApplinkConst.TokopediaNow.TOKOPEDIA_NOW_STAGING_SHOP_ID
        } else {
            shopId == ApplinkConst.TokopediaNow.TOKOPEDIA_NOW_PRODUCTION_SHOP_ID_1 || shopId == ApplinkConst.TokopediaNow.TOKOPEDIA_NOW_PRODUCTION_SHOP_ID_2
        }
    }

    fun getRegisteredWishlistCollectionDetail(uri: Uri, idList: List<String>?): String {
        return if (uri.queryParameterNames.contains(AFFILIATE_UNIQUE_ID)) {
            UriUtil.buildUri(
                ApplinkConstInternalPurchasePlatform.WISHLIST_COLLECTION_DETAIL_WITH_AFFILIATE_UUID,
                idList?.getOrNull(0),
                uri.getQueryParameter(AFFILIATE_UNIQUE_ID)
            )
        } else {
            UriUtil.buildUri(ApplinkConstInternalPurchasePlatform.WISHLIST_COLLECTION_DETAIL_INTERNAL, idList?.getOrNull(0))
        }
    }
}
