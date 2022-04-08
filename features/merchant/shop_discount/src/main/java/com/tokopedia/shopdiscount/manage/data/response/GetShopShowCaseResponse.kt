package com.tokopedia.shopdiscount.manage.data.response


import com.google.gson.annotations.SerializedName

data class GetShopShowCaseResponse(
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
            @SerializedName("aceDefaultSort")
            val aceDefaultSort: Int = 0,
            @SerializedName("alias")
            val alias: String = "",
            @SerializedName("badge")
            val badge: String = "",
            @SerializedName("count")
            val count: Int = 0,
            @SerializedName("highlighted")
            val highlighted: Boolean = false,
            @SerializedName("id")
            val id: String = "",
            @SerializedName("name")
            val name: String = "",
            @SerializedName("type")
            val type: Int = 0,
            @SerializedName("uri")
            val uri: String = "",
            @SerializedName("useAce")
            val useAce: Boolean = false
        )
    }
}