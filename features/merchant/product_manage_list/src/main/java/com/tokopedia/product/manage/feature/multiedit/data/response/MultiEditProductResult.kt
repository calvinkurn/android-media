package com.tokopedia.product.manage.feature.multiedit.data.response

import com.google.gson.annotations.SerializedName

data class MultiEditProductResult (
    @SerializedName("productID")
    val productID: String,
    @SerializedName("result")
    val result: Result? = null
) {
    data class Header(
        @SerializedName("messages")
        val messages: List<String>? = null,
        @SerializedName("reason")
        val reason: String? = null,
        @SerializedName("errorCode")
        val errorCode: String? = null
    )

    data class Result(
        @SerializedName("header")
        val header: Header? = null,
        @SerializedName("isSuccess")
        val isSuccess: Boolean? = null
    )

    fun isSuccess() = result?.isSuccess == true
}