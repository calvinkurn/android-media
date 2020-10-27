package com.tokopedia.qrscanner.event_redeem.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EventRedeem(
        @SerializedName("url")
        @Expose
        val url : String = "",
        @SerializedName("method")
        @Expose
        val method : String = ""
)