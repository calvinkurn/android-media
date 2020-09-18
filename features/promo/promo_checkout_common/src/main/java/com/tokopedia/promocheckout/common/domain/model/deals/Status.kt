package com.tokopedia.promocheckout.common.domain.model.deals

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Status (
        @SerializedName("result")
        @Expose
        val result: String = "",
        @SerializedName("message")
        @Expose
        val message: Message = Message(),
        @SerializedName("code")
        @Expose
        val code: Int = 0
)