package com.tokopedia.topads.auto.data.network.response


import com.google.gson.annotations.SerializedName

data class ErrorResponse(
        @SerializedName("extensions")
        val extensions: Extensions = Extensions(),
        @SerializedName("message")
        val message: String = "",
        @SerializedName("path")
        val path: List<String> = listOf(String())
) {
    class Extensions()
}