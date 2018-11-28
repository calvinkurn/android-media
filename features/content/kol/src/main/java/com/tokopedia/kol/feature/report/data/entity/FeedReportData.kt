package com.tokopedia.kol.feature.report.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FeedReportData(
        @SerializedName("success")
        @Expose
        val success: Int = 0
)