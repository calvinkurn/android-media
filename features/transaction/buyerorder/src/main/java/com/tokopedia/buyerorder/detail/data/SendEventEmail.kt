package com.tokopedia.buyerorder.detail.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SendEventEmail(
    @SerializedName("data")
    @Expose
    val data : DataEmail = DataEmail()
)

data class DataEmail(
        @SerializedName("message")
        @Expose
        val message : String = ""
)