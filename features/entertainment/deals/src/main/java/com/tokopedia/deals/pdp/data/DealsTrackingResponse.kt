package com.tokopedia.deals.pdp.data

import com.google.gson.annotations.SerializedName

data class DealsTrackingResponse(
    @SerializedName("data")
    val data: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("server_process_time")
    val serverProcessTime: String = ""
)
