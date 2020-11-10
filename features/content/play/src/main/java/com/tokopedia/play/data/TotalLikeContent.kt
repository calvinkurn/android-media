package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2019-12-10.
 */
data class TotalLikeContent(
        @SerializedName("reportData")
        val data: List<Data> = emptyList()
) {
    data class Data(
            @SerializedName("channel")
            val channel: Channel = Channel()
    )

    data class Channel(
            @SerializedName("metrics")
            val metrics: Metric = Metric()
    )

    data class Metric(
            @SerializedName("likeChannel") // TODO: wait for update from BE side
            val fmt: String = "",

            @SerializedName("likeChannel")
            val value: String = ""
    )

    data class Response(
            @SerializedName("broadcasterReportSummariesBulk")
            val totalLikeContent: TotalLikeContent = TotalLikeContent()
    )
}