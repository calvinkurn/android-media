package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.play.broadcaster.socket.PlaySocketEnum
import com.tokopedia.play.broadcaster.socket.PlaySocketType


/**
 * Created by mzennis on 22/06/20.
 */
data class LiveStats(
        @SerializedName("channel_id")
        val channelId: String = "",
        @SerializedName("add_to_cart")
        val addToCart: String = "",
        @SerializedName("add_to_cart_fmt")
        val addToCartFmt: String = "",
        @SerializedName("payment_verified")
        val paymentVerified: String = "",
        @SerializedName("payment_verified_fmt")
        val paymentVerifiedFmt: String = "",
        @SerializedName("follow_shop")
        val followShop: String = "",
        @SerializedName("follow_shop_fmt")
        val followShopFmt: String = "",
        @SerializedName("like_channel")
        val likeChannel: String = "",
        @SerializedName("like_channel_fmt")
        val likeChannelFmt: String = "",
        @SerializedName("total_like")
        val totalLike: String = "",
        @SerializedName("total_like_fmt")
        val totalLikeFmt: String = "",
        @SerializedName("visit_shop")
        val visitShop: String = "",
        @SerializedName("visit_shop_fmt")
        val visitShopFmt: String = "",
        @SerializedName("visit_pdp")
        val visitPdp: String = "",
        @SerializedName("visit_pdp_fmt")
        val visitPdpFmt: String = "",
        @SerializedName("visit_channel")
        val visitChannel: String = "",
        @SerializedName("visit_channel_fmt")
        val visitChannelFmt: String = ""
): PlaySocketType {
        override val type: PlaySocketEnum get() = PlaySocketEnum.LiveStats
}