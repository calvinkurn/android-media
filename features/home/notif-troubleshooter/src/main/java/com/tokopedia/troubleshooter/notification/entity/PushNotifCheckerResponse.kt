package com.tokopedia.troubleshooter.notification.entity

import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

data class PushNotifCheckerResponse(
        @NotNull
        @SerializedName("notifier_sendTroubleshooter")
        val notifierSendTroubleshooter: NotifierSendTroubleshooter = NotifierSendTroubleshooter()
)

data class NotifierSendTroubleshooter(
        @SerializedName("is_success")
        val isSuccess: Int = 0,
        @SerializedName("error_message")
        val errorMessage: String = ""
)
