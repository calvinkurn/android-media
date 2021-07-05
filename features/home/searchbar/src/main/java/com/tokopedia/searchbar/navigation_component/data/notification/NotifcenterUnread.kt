package com.tokopedia.searchbar.navigation_component.data.notification


import com.google.gson.annotations.SerializedName

data class NotifcenterUnread(
    @SerializedName("notif_unread")
    val notifUnread: String = ""
)