package com.tokopedia.shop.score.penalty.domain.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopScorePenaltyTypes(
    @Expose
    @SerializedName("error")
    val error: Error = Error(),
    @Expose
    @SerializedName("result")
    val result: List<Result> = listOf()
) {
    data class Result(
        @Expose
        @SerializedName("description")
        val description: String = "",
        @Expose
        @SerializedName("id")
        val id: String = "0",
        @Expose
        @SerializedName("name")
        val name: String = "",
        @Expose
        @SerializedName("penalty")
        val penalty: Int = 0
    )

    data class Error(
        @Expose
        @SerializedName("message")
        val message: String = ""
    )
}