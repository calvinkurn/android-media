package com.tokopedia.shop.home.data.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.data.source.cloud.model.LabelGroup

data class ShopLayoutWidgetParamsModel(
        var shopId: String = "",
        var status: String = "",
        var layoutId: String = "",
        var districtId: String = "",
        var cityId: String = "",
        var latitude: String = "",
        var longitude: String = ""
)