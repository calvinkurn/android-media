package com.tokopedia.mvc.data.response


import com.google.gson.annotations.SerializedName

data class ShopShowcasesByShopIDResponse(
    @SerializedName("shopShowcasesByShopID")
    val shopShowcasesByShopID: ShopShowcasesByShopID = ShopShowcasesByShopID()
) {
    data class ShopShowcasesByShopID(
        @SerializedName("error")
        val error: Error = Error(),
        @SerializedName("result")
        val result: List<Result> = listOf()
    ) {
        data class Error(
            @SerializedName("message")
            val message: String = ""
        )

        data class Result(
            @SerializedName("alias")
            val alias: String = "",
            @SerializedName("id")
            val id: String = "",
            @SerializedName("name")
            val name: String = "",
            @SerializedName("type")
            val type: Int = 0
        )
    }
}
