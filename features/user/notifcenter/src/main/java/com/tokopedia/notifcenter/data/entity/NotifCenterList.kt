package com.tokopedia.notifcenter.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NotifCenterList(
        @Expose
        @SerializedName("paging")
        val paging: Paging = Paging(),
        @Expose
        @SerializedName("list")
        val list: List<UserNotification> = ArrayList()
)