package com.tokopedia.sellerappwidget.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 14/12/20
 */

data class NotificationsModel(
        @SerializedName("chat")
        val chat: ChatModel? = ChatModel(),
        @SerializedName("sellerOrderStatus")
        val sellerOrderStatus: SellerOrderStatusModel? = SellerOrderStatusModel()
)

data class SellerOrderStatusModel(
        @SerializedName("newOrder")
        val newOrder: Int? = 0,
        @SerializedName("readyToShip")
        val readyToShip: Int? = 0
)

data class ChatModel(
        @SerializedName("unreadsSeller")
        val unreadsSeller: Int? = 0
)