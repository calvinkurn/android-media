package com.tokopedia.seller.menu.data.model

import com.google.gson.annotations.SerializedName

data class SellerMenuNotificationResponse(
    @SerializedName("notifications")
    val notifications: Notifications
) {

    data class Notifications(
        @SerializedName("sellerOrderStatus")
        val sellerOrderStatus: SellerOrderStatus,
        @SerializedName("notifcenter_total_unread")
        val notifCenterTotalUnread: NotifCenterTotalUnread,
        @SerializedName("inbox")
        val inbox: Inbox
    )

    data class SellerOrderStatus(
        @SerializedName("newOrder")
        val newOrder: Int,
        @SerializedName("readyToShip")
        val readyToShip: Int,
        @SerializedName("inResolution")
        val inResolution: Int
    )

    data class NotifCenterTotalUnread(
        @SerializedName("notif_total_unread_seller_int")
        val seller: Int
    )

    data class Inbox(
        @SerializedName("talk")
        val talk: Int
    )
}