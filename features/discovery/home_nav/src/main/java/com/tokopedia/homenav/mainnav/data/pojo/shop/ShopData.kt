package com.tokopedia.homenav.mainnav.data.pojo.shop

import com.google.gson.annotations.SerializedName

data class ShopData(
        @SerializedName("userShopInfo")
        val userShopInfo: ShopInfoPojo = ShopInfoPojo(),
        @SerializedName("notifications")
        val notifications: NotificationPojo = NotificationPojo()
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
            val sellerOrderStatus: SellerOrderStatus = SellerOrderStatus()
    ) {
        data class SellerOrderStatus(
                @SerializedName("newOrder")
                val newOrderCount: Int = 0,
                @SerializedName("readyToShip")
                val readyToShipOrderCount: Int = 0,
                @SerializedName("inResolution")
                val inResolution: Int = 0
        )
    }
}