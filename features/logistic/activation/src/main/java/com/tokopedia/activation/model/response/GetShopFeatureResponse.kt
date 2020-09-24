package com.tokopedia.activation.model.response

import com.google.gson.annotations.SerializedName

class GetShopFeatureResponse (
        @SerializedName("shopFeature")
        var shopFeature: ShopFeature = ShopFeature()
)

data class ShopFeature(
        @SerializedName("data")
        var shopData: ShopData = ShopData()
)

data class ShopData(
        @SerializedName("title")
        var title: String = "",
        @SerializedName("type")
        var type: Int = 0,
        @SerializedName("value")
        var value: Boolean = false
)