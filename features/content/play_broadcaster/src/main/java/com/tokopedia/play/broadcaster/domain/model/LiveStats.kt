package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.play.broadcaster.data.type.PlaySocketEnum
import com.tokopedia.play.broadcaster.data.type.PlaySocketType


/**
 * Created by mzennis on 22/06/20.
 */
data class LiveStats(
    @SerializedName("addToCartFmt")
        val addToCart: String = "",
    @SerializedName("productSoldQtyFmt")
        val productSlotQuantity: String = "",
    @SerializedName("followShopFmt")
        val followShop: String = "",
    @SerializedName("likeChannelFmt")
        val likeChannel: String = "",
    @SerializedName("visitShopFmt")
        val visitShop: String = "",
    @SerializedName("visitPDPFmt")
        val visitPdp: String = "",
    @SerializedName("visitChannelFmt")
        val visitChannel: String = ""
): PlaySocketType {
        override val type: PlaySocketEnum get() = PlaySocketEnum.LiveStats
}
