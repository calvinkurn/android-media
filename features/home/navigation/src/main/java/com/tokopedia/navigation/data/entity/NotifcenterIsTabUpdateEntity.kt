package com.tokopedia.navigation.data.entity


import com.google.gson.annotations.SerializedName

data class NotifcenterIsTabUpdateEntity(
        @SerializedName("notifications")
        val notifications: Notifications = Notifications()
)