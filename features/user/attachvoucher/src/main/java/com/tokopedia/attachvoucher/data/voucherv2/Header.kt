package com.tokopedia.attachvoucher.data.voucherv2


import com.google.gson.annotations.SerializedName

data class Header(
    @SerializedName("error_code")
    val errorCode: String = "",
    @SerializedName("message")
    val message: List<Any> = listOf(),
    @SerializedName("process_time")
    val processTime: Int = 0,
    @SerializedName("reason")
    val reason: String = ""
)