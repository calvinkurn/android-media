package com.tokopedia.topads.data.response


import com.google.gson.annotations.SerializedName

data class ResponseEtalase(
    @SerializedName("data")
    val `data`: Data = Data()
) {
    data class Data(
        @SerializedName("shopShowcasesByShopID")
        val shopShowcasesByShopID: ShopShowcasesByShopID = ShopShowcasesByShopID()
    ) {
        data class ShopShowcasesByShopID(
            @SerializedName("result")
            val result: List<Result> = listOf()
        ) {
            data class Result (
                @SerializedName("count")
                val count: Int = 0,
                @SerializedName("id")
                val id: String = "",
                @SerializedName("name")
                val name: String = "",
                @SerializedName("type")
                val type: Int = 0
            )
        }
    }
}