package com.tokopedia.shop.home.data.model

data class ShopLayoutWidgetParamsModel(
    var shopId: String = "",
    var status: String = "",
    var layoutId: String = "",
    var districtId: String = "",
    var cityId: String = "",
    var latitude: String = "",
    var longitude: String = "",
    var listWidgetRequest: List<ShopPageWidgetRequestModel> = listOf()
)
