package com.tokopedia.promocheckout.common.domain.model.deals

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DealsVerifyResponse (
        @SerializedName("status")
        @Expose
        val status: String = "",
        @SerializedName("config")
        @Expose
        val config: String = "",
        @SerializedName("server_process_time")
        @Expose
        val serverProcessTime: String = "",
        @SerializedName("data")
        @Expose
        val data: Data = Data(),
        @SerializedName("message_error")
        @Expose
        val message_error: List<String> = emptyList()
)