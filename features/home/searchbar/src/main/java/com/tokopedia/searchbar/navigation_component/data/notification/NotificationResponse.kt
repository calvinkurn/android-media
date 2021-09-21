package com.tokopedia.searchbar.navigation_component.data.notification


import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("notifcenter_unread")
    val notifcenterUnread: NotifcenterUnread = NotifcenterUnread(),
    @SerializedName("notifications")
    val notifications: Notifications = Notifications(),
    @SerializedName("status")
    val status: String = "",
    @SerializedName("userShopInfo")
    val userShopInfo: UserShopInfo = UserShopInfo()
)