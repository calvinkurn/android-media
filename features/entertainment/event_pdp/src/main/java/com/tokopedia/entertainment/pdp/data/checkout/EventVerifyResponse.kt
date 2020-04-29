package com.tokopedia.entertainment.pdp.data.checkout


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EventVerifyResponse(
        @SerializedName("config")
        @Expose
        val config: String = "",
        @SerializedName("data")
        @Expose
        val data: Data = Data(),
        @SerializedName("server_process_time")
        @Expose
        val serverProcessTime: String = "",
        @SerializedName("status")
        @Expose
        val status: String = ""
)