package com.tokopedia.shop.settings.basicinfo.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHour

data class SetShopOperationalHoursRequestParams(
        @SerializedName("shopID")
        var shopId: String = "",
        @SerializedName("type")
        var type: Int = 1,
        @SerializedName("params")
        var newOpsHourList: List<ShopOperationalHour> = listOf()
)