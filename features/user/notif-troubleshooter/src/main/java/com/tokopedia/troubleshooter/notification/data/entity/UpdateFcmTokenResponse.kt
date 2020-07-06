package com.tokopedia.troubleshooter.notification.data.entity

import com.google.gson.annotations.SerializedName

data class UpdateFcmTokenResponse(
    @SerializedName("notifier_updateGcm")
    val notifierUpdateGcm: NotifierUpdateGcm = NotifierUpdateGcm()
) {

    fun isUpdateTokenSuccess(): Boolean {
        return notifierUpdateGcm.isSuccess == 1
    }

    fun errorMessage(): String {
        return notifierUpdateGcm.errorMessage
    }

}

data class NotifierUpdateGcm(
        @SerializedName("error_message")
        val errorMessage: String = "",

        @SerializedName("is_success")
        val isSuccess: Int = -1,

        @SerializedName("status")
        val status: Int = -1
)