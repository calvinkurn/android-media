package com.tokopedia.promocheckout.common.domain.model.deals

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DealsErrorResponse(
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
        val data: ErrorData = ErrorData(),
        @SerializedName("message_error")
        @Expose
        val message_error: List<String> = emptyList()
)

data class ErrorData(
        @SerializedName("message")
        @Expose
        val message: String = "",
        @SerializedName("code")
        @Expose
        val code: String = "",
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        @SerializedName("message_error")
        @Expose
        val message_error: String = ""
)