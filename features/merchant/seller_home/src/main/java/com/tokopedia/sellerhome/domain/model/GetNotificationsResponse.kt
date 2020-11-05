package com.tokopedia.sellerhome.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetNotificationsResponse(
        @Expose
        @SerializedName("notifications")
        val notifications: NotificationsModel?,
        @Expose
        @SerializedName("notifcenter_unread")
        val notifCenterUnread: NotifCenterUnreadModel?
)

data class NotificationsModel(
        @Expose
        @SerializedName("chat")
        val chat: ChatModel?,
        @Expose
        @SerializedName("notifcenter_unread")
        val notifCenterUnread: NotifCenterUnreadModel?,
        @Expose
        @SerializedName("sellerOrderStatus")
        val sellerOrderStatus: SellerOrderStatusModel?
)

data class ChatModel(
        @Expose
        @SerializedName("unreadsSeller")
        val unreadsSeller: Int = 0
)

data class NotifCenterUnreadModel(
        @SerializedName("notif_unread_int")
        val notifUnreadInt: Int = 0
)

data class SellerOrderStatusModel(
        @Expose
        @SerializedName("newOrder")
        val newOrder: Int = 0,
        @Expose
        @SerializedName("readyToShip")
        val readyToShip: Int = 0
)