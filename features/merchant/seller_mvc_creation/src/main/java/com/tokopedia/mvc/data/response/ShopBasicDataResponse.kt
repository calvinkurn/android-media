package com.tokopedia.mvc.data.response


import com.google.gson.annotations.SerializedName

data class ShopBasicDataResponse(
    @SerializedName("shopBasicData")
    val shopBasicData: ShopBasicData = ShopBasicData()
) {
    data class ShopBasicData(
        @SerializedName("error")
        val error: Error = Error(),
        @SerializedName("result")
        val result: Result = Result()
    ) {
        data class Error(
            @SerializedName("message")
            val message: String = ""
        )

        data class Result(
            @SerializedName("domain")
            val domain: String = "",
            @SerializedName("logo")
            val logo: String = "",
            @SerializedName("name")
            val name: String = ""
        )
    }
}
