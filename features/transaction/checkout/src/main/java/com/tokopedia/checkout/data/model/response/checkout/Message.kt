package com.tokopedia.checkout.data.model.response.checkout

import com.google.gson.annotations.SerializedName

data class Message(
        @SerializedName("title")
        val title: String = "",
        @SerializedName("desc")
        val desc: String = "",
        @SerializedName("action")
        val action: String = ""
)