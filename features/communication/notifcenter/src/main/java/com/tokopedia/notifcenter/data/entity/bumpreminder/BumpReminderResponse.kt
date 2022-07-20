package com.tokopedia.notifcenter.data.entity.bumpreminder


import com.google.gson.annotations.SerializedName

data class BumpReminderResponse(
        @SerializedName("notifcenter_setReminderBump")
        val notifcenterSetReminderBump: NotifcenterSetReminderBump = NotifcenterSetReminderBump()
)