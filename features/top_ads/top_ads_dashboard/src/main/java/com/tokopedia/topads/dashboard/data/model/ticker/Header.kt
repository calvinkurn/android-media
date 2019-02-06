package com.tokopedia.topads.dashboard.data.model.ticker

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Header(
    @SerializedName("process_time")
    @Expose
    val processTime: Double = 0.0,

    @SerializedName("total_data")
    @Expose
    var totalData: Int = 0
)
