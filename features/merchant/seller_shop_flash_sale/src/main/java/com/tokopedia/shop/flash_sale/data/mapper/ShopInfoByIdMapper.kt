package com.tokopedia.shop.flash_sale.data.mapper

import com.tokopedia.shop.flash_sale.common.constant.Constant.EMPTY_STRING
import com.tokopedia.shop.flash_sale.common.constant.Constant.ZERO
import com.tokopedia.shop.flash_sale.data.response.ShopInfoByIdResponse
import com.tokopedia.shop.flash_sale.domain.entity.ShopInfo
import javax.inject.Inject

class ShopInfoByIdMapper @Inject constructor() {

    companion object {
        private const val TRUE = 1
        private const val OFFICIAL_STORE = 1
    }

    fun map(data: ShopInfoByIdResponse): ShopInfo {
        return if (data.shopInfoByID.result.isEmpty()) {
            ShopInfo(ZERO, EMPTY_STRING, ZERO, EMPTY_STRING, isPowerMerchant = false, isOfficial = false)
        } else {
            val shop = data.shopInfoByID.result[ZERO]
            return ShopInfo(
                shop.goldOS.shopTier,
                shop.goldOS.shopTierWording,
                shop.goldOS.shopGrade,
                shop.goldOS.shopGradeWording,
                shop.goldOS.isGold == TRUE,
                shop.goldOS.isOfficial == TRUE
            )
        }


    }


}