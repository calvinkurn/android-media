package com.tokopedia.product_bundle.common.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Error(
        @Expose
        @SerializedName("messages")
        val messages: String = "",
        @Expose
        @SerializedName("reason")
        val reason: String = "",
        @Expose
        @SerializedName("errorCode")
        val errorCode: String = ""
)