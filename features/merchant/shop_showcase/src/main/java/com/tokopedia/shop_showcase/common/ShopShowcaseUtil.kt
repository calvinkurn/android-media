package com.tokopedia.shop_showcase.common

import com.tokopedia.shop.common.constant.TrackShopTypeDef

object ShopShowcaseUtil {

    fun getShopType(isOfficialStore: Boolean, isGoldMerchant: Boolean): String {
        return when {
            isOfficialStore -> TrackShopTypeDef.OFFICIAL_STORE
            isGoldMerchant -> TrackShopTypeDef.GOLD_MERCHANT
            else -> TrackShopTypeDef.REGULAR_MERCHANT
        }
    }

    fun isMyShop(shopId: String, userSessionShopId: String)  = shopId == userSessionShopId

}