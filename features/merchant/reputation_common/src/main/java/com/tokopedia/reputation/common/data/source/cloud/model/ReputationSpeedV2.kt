package com.tokopedia.reputation.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReputationSpeedV2(
        @SerializedName("hour")
        @Expose val hour: Int,

        @SerializedName("day")
        @Expose val day: Int,

        @SerializedName("speed_fmt")
        @Expose val speedFmt: String,

        @SerializedName("total_order")
        @Expose val totalOrder: Int
)