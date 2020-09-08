package com.tokopedia.activation.model

import com.google.gson.annotations.SerializedName

class GetShopFeatureResponse (
        @SerializedName("data")
        var data: Data = Data()
)

data class Data(
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