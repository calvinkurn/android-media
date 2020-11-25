package com.tokopedia.promocheckout.common.domain.model.event

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Status(
        @SerializedName("code")
        @Expose
        val code: Int = 0,
        @SerializedName("message")
        @Expose
        val message: Message = Message(),
        @SerializedName("result")
        @Expose
        val result: String = ""
)