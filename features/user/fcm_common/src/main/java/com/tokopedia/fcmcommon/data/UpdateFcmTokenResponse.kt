package com.tokopedia.fcmcommon.data


import com.google.gson.annotations.SerializedName

data class UpdateFcmTokenResponse(
    @SerializedName("notifier_updateGcm")
    val notifierUpdateGcm: NotifierUpdateGcm = NotifierUpdateGcm()
) {

    fun updateTokenSuccess(): Boolean {
        return notifierUpdateGcm.isSuccess == 1
    }

    fun getErrorMessage(): String {
        return notifierUpdateGcm.errorMessage
    }
}