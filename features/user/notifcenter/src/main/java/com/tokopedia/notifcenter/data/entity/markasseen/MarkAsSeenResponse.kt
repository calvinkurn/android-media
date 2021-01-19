package com.tokopedia.notifcenter.data.entity.markasseen


import com.google.gson.annotations.SerializedName

data class MarkAsSeenResponse(
    @SerializedName("notifcenter_markSeenNotif")
    val notifcenterMarkSeenNotif: NotifcenterMarkSeenNotif = NotifcenterMarkSeenNotif()
)