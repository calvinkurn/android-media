package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 04/06/20
 */
data class GetLiveStatisticsResponse(
        @SerializedName("broadcasterReportLiveSummaries")
        @Expose
        val reportChannelSummary: ReportChannelSummary = ReportChannelSummary()
) {
        data class ReportChannelSummary(
                @SerializedName("channel")
                @Expose
                val channel: Channel = Channel(),
                @SerializedName("duration")
                @Expose
                val duration: String = ""
        )

        data class Channel(
                @SerializedName("channelID")
                @Expose
                val channelId: String = "",

                @SerializedName("metrics")
                @Expose
                val metrics: LiveStats = LiveStats(),

                @SerializedName("userMetrics")
                @Expose
                val userMetrics: ReportUserChannelMetric = ReportUserChannelMetric()
        )

    data class ReportUserChannelMetric(
        @SerializedName("visitChannelFmt")
        val visitChannel: String = "",
        @SerializedName("totalLikeFmt")
        val likeChannel: String = "",
        @SerializedName("followProfileFmt")
        val followProfile: String = "",
        @SerializedName("visitPDPFmt")
        val visitPdp: String = "",
        @SerializedName("visitProfileFmt")
        val visitProfile: String = "",
        @SerializedName("addToCartFmt")
        val addToCart: String = "",
        @SerializedName("productSoldQtyFmt")
        val productSlotQuantity: String = "",
    )
}
