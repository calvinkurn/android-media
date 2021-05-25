package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class Button(
        @SerializedName("code")
        val code: String = "",
        @SerializedName("color")
        val color: String = "",
        @SerializedName("id")
        val id: String = "",
        @SerializedName("message")
        val message: String = ""
)