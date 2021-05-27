package com.tokopedia.shop.settings.basicinfo.view.model

import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHour

data class ShopSettingsOperationalHoursListUiModel(
        var closeInfo: ShopInfo.ClosedInfo = ShopInfo.ClosedInfo(),
        var operationalHourList: List<ShopOperationalHour> = listOf(),
        var error: Throwable? = null
)