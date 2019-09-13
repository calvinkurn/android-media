package com.tokopedia.home.account.data.model

import com.google.gson.annotations.SerializedName

data class PushNotifCheckerResponse(
        @SerializedName("notifier_sendTroubleshooter")
        val notifierSendTroubleshooter: NotifierSendTroubleshooter
)

data class NotifierSendTroubleshooter(
        @SerializedName("is_success")
        val isSuccess: Int,
        @SerializedName("error_message")
        val errorMessage: String
)
