package com.tokopedia.fcmcommon.data


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UpdateFcmTokenResponse(
    @SerializedName("notifier_updateGcm")
    @Expose
    val notifierUpdateGcm: NotifierUpdateGcm = NotifierUpdateGcm()
) {

    fun updateTokenSuccess(): Boolean {
        return notifierUpdateGcm.isSuccess == 1
    }

    fun getErrorMessage(): String {
        return notifierUpdateGcm.errorMessage
    }
}