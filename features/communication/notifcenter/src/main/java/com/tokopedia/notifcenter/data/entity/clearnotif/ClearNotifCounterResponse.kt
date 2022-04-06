package com.tokopedia.notifcenter.data.entity.clearnotif


import com.google.gson.annotations.SerializedName

data class ClearNotifCounterResponse(
        @SerializedName("notifcenter_clearNotifCounter")
        val notifcenterClearNotifCounter: NotifcenterClearNotifCounter = NotifcenterClearNotifCounter()
)