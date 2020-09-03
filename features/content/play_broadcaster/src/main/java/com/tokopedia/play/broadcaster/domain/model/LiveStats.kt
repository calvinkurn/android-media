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
        @SerializedName("remove_from_cart")
        val removeFromCart: String = "",
        @SerializedName("wish_list")
        val wishList: String = "",
        @SerializedName("remove_wish_list")
        val removeWishList: String = "",
        @SerializedName("payment_verified")
        val paymentVerified: String = "",
        @SerializedName("follow_shop")
        val followShop: String = "",
        @SerializedName("unfollow_shop")
        val unFollowShop: String = "",
        @SerializedName("like_channel")
        val likeChannel: String = "",
        @SerializedName("like_channel_fmt")
        val likeChannelFmt: String = "",
        @SerializedName("unlike_channel")
        val unlikeChannel: String = "",
        @SerializedName("unlike_channel_fmt")
        val unlikeChannelFmt: String = "",
        @SerializedName("total_like")
        val totalLike: String = "",
        @SerializedName("total_like_fmt")
        val totalLikeFmt: String = "",
        @SerializedName("visit_shop")
        val visitShop: String = "",
        @SerializedName("visit_pdp")
        val visitPdp: String = "",
        @SerializedName("visit_channel")
        val visitChannel: String = ""
): PlaySocketType {
        override val type: PlaySocketEnum get() = PlaySocketEnum.LiveStats
}