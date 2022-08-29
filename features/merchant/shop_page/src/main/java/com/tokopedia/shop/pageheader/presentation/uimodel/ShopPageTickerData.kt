package com.tokopedia.shop.pageheader.presentation.uimodel

import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopoperationalhourstatus.ShopOperationalHourStatus

data class ShopPageTickerData(
        val shopInfo: ShopInfo = ShopInfo(),
        val shopOperationalHourStatus: ShopOperationalHourStatus = ShopOperationalHourStatus()
)