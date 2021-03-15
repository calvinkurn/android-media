package com.tokopedia.devicefingerprint.appauth.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AppAuthResponse(
        @SerializedName("mutationSignDvc")
        @Expose
        val mutationSignDvc: StatusMessage = StatusMessage()
)

data class StatusMessage(
        @SerializedName("status")
        @Expose
        val status: Boolean = false,
        @SerializedName("error_message")
        @Expose
        val errorMessage: String = ""
) {
        var isSuccess = status && errorMessage.isEmpty()
}