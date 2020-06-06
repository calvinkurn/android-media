package com.tokopedia.play.broadcaster.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 04/06/20
 */

data class BroadcasterReportLiveSummaries(
        @SerializedName("channel")
        @Expose
        val channel: Channel = Channel()
) {
    data class Response(
            @SerializedName("broadcasterReportLiveSummaries")
            @Expose
            val response: BroadcasterReportLiveSummaries = BroadcasterReportLiveSummaries()
    )
}

data class Channel(
        @SerializedName("channelID")
        @Expose
        val channelId: String = "",

        @SerializedName("metrics")
        @Expose
        val metrics: Metrics = Metrics()
)

data class Metrics (
        @SerializedName("addToCart")
        @Expose
        val addToCart: String = "",

        @SerializedName("removeFromCart")
        @Expose
        val removeFromCart: String = "",

        @SerializedName("wishList")
        @Expose
        val wishList: String = "",

        @SerializedName("removeWishList")
        @Expose
        val removeWishList: String = "",

        @SerializedName("paymentVerified")
        @Expose
        val paymentVerified: String = "",

        @SerializedName("followShop")
        @Expose
        val followShop: String = "",

        @SerializedName("unFollowShop")
        @Expose
        val unFollowShop: String = "",

        @SerializedName("likeChannel")
        @Expose
        val likeChannel: String = "",

        @SerializedName("unLike")
        @Expose
        val unLike: String = "",

        @SerializedName("visitShop")
        @Expose
        val visitShop: String = "",

        @SerializedName("visitPDP")
        @Expose
        val visitPDP: String = "",

        @SerializedName("visitChannel")
        @Expose
        val visitChannel: String = ""
)