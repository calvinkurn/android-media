package com.tokopedia.shop.home.data.model

import com.tokopedia.shop.common.data.model.Options

data class ShopLayoutWidgetParamsModel(
    var shopId: String = "",
    var status: String = "",
    var layoutId: String = "",
    var districtId: String = "",
    var cityId: String = "",
    var latitude: String = "",
    var longitude: String = "",
    var listWidgetRequest: List<ShopPageWidgetRequestModel> = listOf(),
    var options: List<Options> = emptyList()
)
