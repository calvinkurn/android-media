package com.tokopedia.telemetry.network.data

import com.google.gson.annotations.SerializedName

data class MutationSubDvcTlRequest(
        @SerializedName("event")
        val event: String = "",
        @SerializedName("raw_data")
        val rawData: String = "",
        @SerializedName("start_time")
        val startTime: Int = 0,
        @SerializedName("end_time")
        val endTime: Int = 0,
        @SerializedName("pvt_string")
        val pvtString: String = ""
)