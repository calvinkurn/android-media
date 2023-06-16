package com.tokopedia.content.common.model

import com.google.gson.annotations.SerializedName

/**
 * Created by shruti agarwal on 13/02/23.
 */
data class FeedComplaintSubmitReportResponse(
    @SerializedName("feedsComplaintSubmitReport")
    val data: FeedComplaintSubmitReport = FeedComplaintSubmitReport()
) {
    data class FeedComplaintSubmitReport(
        @SerializedName("success")
        val success: Boolean = false
    )
}
