package com.tokopedia.kol.feature.report.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SendReportResponse(
        @SerializedName("feed_report_submit")
        @Expose
        val feedReportSubmit: FeedReportSubmit = FeedReportSubmit()
)