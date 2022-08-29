package com.tokopedia.vouchercreation.product.list.domain.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Header(
        @SerializedName("processTime")
        @Expose val processTime: Float = 0f,
        @SerializedName("messages")
        @Expose val messages: List<String> = listOf(),
        @SerializedName("reason")
        @Expose val reason: String = "",
        @SerializedName("errorCode")
        @Expose val errorCode: String = ""
)