package com.tokopedia.notifcenter.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-09-11.
 * ade.hadian@tokopedia.com
 */

data class SendNotification(
        @SerializedName("notifcenter_sendNotif")
        @Expose
        var data: NotifCenterSendNotifData = NotifCenterSendNotifData()

)

data class NotifCenterSendNotifData(
        @SerializedName("status")
        @Expose
        var status: String = "",
        @SerializedName("message_error")
        @Expose
        var errorMessage: ArrayList<String> = arrayListOf(),
        @SerializedName("process_time")
        @Expose
        var processTime: Float = 0f,
        @SerializedName("server")
        @Expose
        var server: String = ""
)

data class SendNotifData(
        @SerializedName("is_success")
        @Expose
        var isSuccess: Int = 0,
        @SerializedName("warn_message")
        @Expose
        var warnMessage: ArrayList<String> = arrayListOf(),
        @SerializedName("notification")
        @Expose
        var notification: NotifData = NotifData()
)

data class NotifData(
        @SerializedName("user_id")
        @Expose
        var userId: Int = 0,
        @SerializedName("notif_id")
        @Expose
        var notifId: String = ""
)