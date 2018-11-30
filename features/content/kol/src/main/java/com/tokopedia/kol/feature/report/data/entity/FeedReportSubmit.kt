package com.tokopedia.kol.feature.report.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FeedReportSubmit(
        @SerializedName("data")
        @Expose
        val data: FeedReportData = FeedReportData(),
        @SerializedName("error")
        @Expose
        val error: String = ""
)