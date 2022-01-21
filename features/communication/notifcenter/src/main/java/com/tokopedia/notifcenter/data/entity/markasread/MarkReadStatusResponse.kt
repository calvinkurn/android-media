package com.tokopedia.notifcenter.data.entity.markasread


import com.google.gson.annotations.SerializedName

data class MarkReadStatusResponse(
        @SerializedName("notifcenter_markReadStatus")
        val notifcenterMarkReadStatus: NotifcenterMarkReadStatus = NotifcenterMarkReadStatus()
)