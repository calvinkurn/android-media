package com.tokopedia.feedcomponent.domain.model.report.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FeedReportSubmit(
        @SerializedName("data")
        @Expose
        val data: FeedReportData = FeedReportData(),
        @SerializedName("error_message")
        @Expose
        val errorMessage: String = "",
        @SerializedName("error_type")
        @Expose
        val errorType: String = ""
)