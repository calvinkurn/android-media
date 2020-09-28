package com.tokopedia.troubleshooter.notification.data.entity

import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

data class NotificationTroubleshoot(
        @NotNull @SerializedName("notifier_sendTroubleshooter")
        val notificationSendTroubleshoot: NotificationSendTroubleshoot = NotificationSendTroubleshoot()
)

data class NotificationSendTroubleshoot(
        @SerializedName("is_success")
        val isSuccess: Int = 0,

        @SerializedName("error_message")
        val errorMessage: String = ""
) {
        fun isTroubleshootSuccess(): Boolean {
                return isSuccess == 1
        }
}
