package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by @ilhamsuaib on 24/02/22.
 */

data class CalendarQueryParam(
    @SerializedName("start_date")
    @Expose
    val startDate: String,
    @SerializedName("end_date")
    @Expose
    val endDate: String
)