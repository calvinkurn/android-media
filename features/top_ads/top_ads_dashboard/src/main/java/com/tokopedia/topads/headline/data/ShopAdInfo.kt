package com.tokopedia.topads.headline.data


import com.google.gson.annotations.SerializedName

data class ShopAdInfo(
    @SerializedName("topadsGetShopInfoV2")
    val topadsGetShopInfoV2: TopadsGetShopInfoV2 = TopadsGetShopInfoV2()
)

data class TopadsGetShopInfoV2(
        @SerializedName("data")
        val data: Data = Data(),
        @SerializedName("errors")
        val errors: List<Any> = listOf()
)
data class Data(
        @SerializedName("ads")
        val ads: List<Ad> = listOf()
)

data class Ad(
        @SerializedName("is_used")
        val isUsed: Boolean = false,
        @SerializedName("type")
        val type: String = ""
)