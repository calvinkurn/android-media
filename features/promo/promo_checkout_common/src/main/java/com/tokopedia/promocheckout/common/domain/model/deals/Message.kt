package com.tokopedia.promocheckout.common.domain.model.deals

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Message (
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("message")
        @Expose
        val message: String = ""
)