package com.tokopedia.notifcenter.data.entity.deletereminder


import com.google.gson.annotations.SerializedName

data class DeleteReminderResponse(
        @SerializedName("notifcenter_deleteReminderBump")
        val notifcenterDeleteReminderBump: NotifcenterDeleteReminderBump = NotifcenterDeleteReminderBump()
)