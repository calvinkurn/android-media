package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.play.broadcaster.socket.PlaySocketEnum
import com.tokopedia.play.broadcaster.socket.PlaySocketType


/**
 * Created by mzennis on 22/06/20.
 */
data class LiveStats(
        @SerializedName("channel_id")
        val channelId: Int,
        @SerializedName("add_to_cart")
        val addToCart: Int,
        @SerializedName("remove_from_cart")
        val removeFromCart: Int,
        @SerializedName("wish_list")
        val wishList: Int,
        @SerializedName("remove_wish_list")
        val removeWishList: Int,
        @SerializedName("payment_verified")
        val paymentVerified: Int,
        @SerializedName("follow_shop")
        val followShop: Int,
        @SerializedName("unfollow_shop")
        val unFollowShop: Int,
        @SerializedName("like_channel")
        val likeChannel: Int,
        @SerializedName("like_channel_fmt")
        val likeChannelFmt: String,
        @SerializedName("unlike_channel")
        val unlikeChannel: Int,
        @SerializedName("unlike_channel_fmt")
        val unlikeChannelFmt: String,
        @SerializedName("total_like")
        val totalLike: Int,
        @SerializedName("total_like_fmt")
        val totalLikeFmt: String,
        @SerializedName("visit_shop")
        val visitShop: Int,
        @SerializedName("visit_pdp")
        val visitPdp: Int,
        @SerializedName("visit_channel")
        val visitChannel: Int
): PlaySocketType {
        override val type: PlaySocketEnum get() = PlaySocketEnum.LiveStats
}