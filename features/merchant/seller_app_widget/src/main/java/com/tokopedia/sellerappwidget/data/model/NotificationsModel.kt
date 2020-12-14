package com.tokopedia.sellerappwidget.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 14/12/20
 */

data class NotificationsModel(
        @Expose
        @SerializedName("chat")
        val chat: ChatModel? = ChatModel(),
        @Expose
        @SerializedName("sellerOrderStatus")
        val sellerOrderStatus: SellerOrderStatusModel? = SellerOrderStatusModel()
)

data class SellerOrderStatusModel(
        @Expose
        @SerializedName("newOrder")
        val newOrder: Int? = 0,
        @Expose
        @SerializedName("readyToShip")
        val readyToShip: Int? = 0
)

data class ChatModel(
        @Expose
        @SerializedName("unreadsSeller")
        val unreadsSeller: Int? = 0
)