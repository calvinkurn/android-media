package com.tokopedia.content.common.track.response

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 28/01/21
 */
data class ReportSummaries(
    @SerializedName("reportData")
    val data: List<Data> = emptyList()
) {
    data class Data(
        @SerializedName("content")
        val content: Content = Content()
    )

    data class Content(
        @SerializedName("metrics")
        val metrics: Metric = Metric(),
    )

    data class Metric(

        @SerializedName("totalLike")
        val totalLike: String = "",

        @SerializedName("totalLikeFmt")
        val totalLikeFmt: String = "",

        @SerializedName("visitContent")
        val totalView: String = "",

        @SerializedName("visitContentFmt")
        val totalViewFmt: String = "",
    )

    data class Response(
        @SerializedName("broadcasterReportSummariesBulkV2")
        val reportSummaries: ReportSummaries = ReportSummaries()
    )
}
