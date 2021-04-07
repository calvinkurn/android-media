package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 28/01/21
 */
data class ReportSummaries(
        @SerializedName("reportData")
        val data: List<Data> = emptyList()
) {
    data class Data(
            @SerializedName("channel")
            val channel: Channel = Channel()
    )

    data class Channel(
            @SerializedName("metrics")
            val metrics: Metric = Metric(),
    )

    data class Metric(

            @SerializedName("totalLike")
            val totalLike: String = "",

            @SerializedName("totalLikeFmt")
            val totalLikeFmt: String = "",

            @SerializedName("visitChannel")
            val totalView: String = "",

            @SerializedName("visitChannelFmt")
            val totalViewFmt: String = "",
    )

    data class Response(
            @SerializedName("broadcasterReportSummariesBulk")
            val reportSummaries: ReportSummaries = ReportSummaries()
    )
}