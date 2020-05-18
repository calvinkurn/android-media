package com.tokopedia.promocheckout.common.domain.model.event

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