package com.tokopedia.homenav.mainnav.data.pojo.notif

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NavNotificationPojo(
        @SerializedName("notifications")
        @Expose
        val notifications: Notifications
)