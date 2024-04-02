package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.play.broadcaster.data.type.PlaySocketEnum
import com.tokopedia.play.broadcaster.data.type.PlaySocketType


/**
 * Created by mzennis on 22/06/20.
 */
data class LiveStats(
    @SerializedName("add_to_cart_fmt")
    val addToCart: String = "",
    @SerializedName("product_sold_qty_fmt")
    val productSlotQuantity: String = "",
    @SerializedName("follow_shop_fmt")
    val followShop: String = "",
    @SerializedName("like_channel_fmt")
    val likeChannel: String = "",
    @SerializedName("visit_shop_fmt")
    val visitShop: String = "",
    @SerializedName("visit_pdp_fmt")
    val visitPdp: String = "",
    @SerializedName("visit_channel_fmt")
    val visitChannel: String = "",
    @SerializedName("live_concurrent_users_fmt")
    val liveConcurrentUsers: String = "",
    @SerializedName("estimated_income_fmt")
    val estimatedIncome: String = "",
    @SerializedName("timestamp")
    val timestamp: Long = 0L,
): PlaySocketType {
        override val type: PlaySocketEnum get() = PlaySocketEnum.LiveStats
}
