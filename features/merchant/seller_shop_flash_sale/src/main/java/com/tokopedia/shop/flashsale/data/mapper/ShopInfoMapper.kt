package com.tokopedia.shop.flashsale.data.mapper

import com.tokopedia.shop.flashsale.domain.entity.ShopInfo
import javax.inject.Inject

class ShopInfoMapper @Inject constructor() {

    companion object {
        private const val TRUE = 1
    }

    fun map(shop: com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo): ShopInfo {
        return ShopInfo(shop.goldOS.isGold == TRUE, shop.goldOS.isOfficial == TRUE)
    }


}