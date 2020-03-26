package com.tokopedia.product.manage.feature.cashback.data

import com.google.gson.annotations.SerializedName

data class SetCashbackHeader(
        @SerializedName("process_time")
        val processTime: Float = 0f,
        @SerializedName("message")
        val message: List<String> = listOf(),
        @SerializedName("reason")
        val reason: String = "",
        @SerializedName("error_code")
        val errorCode: String = ""

)