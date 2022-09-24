package com.tokopedia.kolcommon.data

import com.google.gson.annotations.SerializedName

/**
 * Created by meyta.taliti on 03/08/22.
 */
data class SubmitReportContentResponse(
    @SerializedName("feed_report_submit")
    val content: FeedReportSubmit = FeedReportSubmit()
) {
    data class FeedReportSubmit(
        @SerializedName("data")
        val data: FeedReportData = FeedReportData(),

        @SerializedName("error_message")
        val errorMessage: String = "",

        @SerializedName("error_type")
        val errorType: String = ""
    )

    data class FeedReportData(
        @SerializedName("success")
        val success: Int = 0
    )
}