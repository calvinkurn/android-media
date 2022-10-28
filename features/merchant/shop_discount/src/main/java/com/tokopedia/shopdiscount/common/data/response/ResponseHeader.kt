package com.tokopedia.shopdiscount.common.data.response

import com.google.gson.annotations.SerializedName

data class ResponseHeader(
    @SerializedName("error_code")
    val errorCode: String = "",
    @SerializedName("process_time")
    val processTime: Double = 0.0,
    @SerializedName("reason")
    val reason: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("error_message")
    val errorMessages: List<String> = listOf()
)