package com.tokopedia.homenav.mainnav.data.pojo.shop

import com.google.gson.annotations.SerializedName

data class ShopData(
        @SerializedName("userShopInfo")
        val userShopInfo: ShopInfoPojo = ShopInfoPojo(),
        @SerializedName("notifications")
        val notifications: NotificationPojo
) {
    data class ShopInfoPojo(
            @SerializedName("info")
            val info: Info = Info()
    ) {
        data class Info(
                @SerializedName("shop_name")
                val shopName: String = "",
                @SerializedName("shop_id")
                val shopId: String = ""
        )
    }

    data class NotificationPojo(
            @SerializedName("sellerOrderStatus")
            val sellerOrderStatus: SellerOrderStatus
    ) {
        data class SellerOrderStatus(
                @SerializedName("newOrder")
                val newOrderCount: Int,
                @SerializedName("readyToShip")
                val readyToShipOrderCount: Int,
                @SerializedName("inResolution")
                val inResolution: Int
        )
    }
}