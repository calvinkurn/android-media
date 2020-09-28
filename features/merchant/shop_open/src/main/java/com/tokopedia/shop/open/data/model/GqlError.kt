package com.tokopedia.shop.open.data.model

import com.google.gson.annotations.SerializedName


data class GqlError(
        @SerializedName("errors")
        val errors: List<ErrorResponse> = listOf()
)

data class ErrorResponse(
        @SerializedName("extensions")
        val extensions: Extensions = Extensions(),
        @SerializedName("message")
        val message: String = "",
        @SerializedName("path")
        val path: List<String> = listOf()
)

data class Extensions(
        @SerializedName("developerMessage")
        val developerMessage: String = "",
        @SerializedName("moreInfo")
        val moreInfo: String = ""
)