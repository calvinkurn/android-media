package com.tokopedia.entertainment.pdp.data.checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Message(
        @SerializedName("message")
        @Expose
        val message: String = "",
        @SerializedName("title")
        @Expose
        val title: String = ""
)