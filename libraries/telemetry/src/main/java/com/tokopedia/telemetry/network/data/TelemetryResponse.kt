package com.tokopedia.telemetry.network.data

import com.google.gson.annotations.SerializedName

data class TelemetryResponse(
        @SerializedName("subDvcTl")
        val subDvcTl: SubDvcTl
) {
        fun isError() = subDvcTl.isError
}

data class SubDvcTl(
        @SerializedName("is_error")
        val isError: Boolean = false,
        @SerializedName("data")
        val data: DVDataInit
)

data class DVDataInit (
        @SerializedName("error_message")
        val errorMessage: String = "")