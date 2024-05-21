package com.tokopedia.content.common.track.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Jonathan Darwin on 08 March 2024
 */
data class GetReportSummaryResponse(
    @SerializedName("broadcasterReportSummariesBulkV2")
    val data: Data = Data()
) {
    data class Data(
        @SerializedName("reportData")
        val reportData: List<ReportData> = emptyList(),

        @SerializedName("timestamp")
        val timestamp: String = "",
    )

    data class ReportData(
        @SerializedName("content")
        val content: Content = Content(),
    )

    data class Content(
        @SerializedName("metrics")
        val metrics: Metrics = Metrics(),

        @SerializedName("userMetrics")
        val userMetrics: Metrics = Metrics(),
    )

    data class Metrics(
        @SerializedName("liveConcurrentUsersFmt")
        val liveConcurrentUsers: String = "",

        @SerializedName("visitContentFmt")
        val visitContent: String = "",

        @SerializedName("estimatedIncomeFmt")
        val estimatedIncome: String = "",

        @SerializedName("totalLikeFmt")
        val totalLike: String = "",

        @SerializedName("totalLike")
        val totalLikeRaw: String = "",
    )
}
