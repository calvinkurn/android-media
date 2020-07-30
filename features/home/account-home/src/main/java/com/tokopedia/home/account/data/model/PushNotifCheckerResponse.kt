package com.tokopedia.home.account.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

data class PushNotifCheckerResponse(
        @NotNull
        @SerializedName("notifier_sendTroubleshooter")
        @Expose
        val notifierSendTroubleshooter: NotifierSendTroubleshooter
)

data class NotifierSendTroubleshooter(
        @SerializedName("is_success")
        @Expose
        val isSuccess: Int,
        @SerializedName("error_message")
        @Expose
        val errorMessage: String
)
