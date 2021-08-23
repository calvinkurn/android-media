package com.tokopedia.product_bundle.common.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Header(
        @Expose
        @SerializedName("ErrorCode")
        val errorCode: String = "",
        @Expose
        @SerializedName("Reason")
        val reason: String = "",
        @Expose
        @SerializedName("Message")
        val messages: List<String> = listOf()
)