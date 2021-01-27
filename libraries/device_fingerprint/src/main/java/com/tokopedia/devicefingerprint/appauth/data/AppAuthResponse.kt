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
        val status: String = "",
        @SerializedName("message")
        @Expose
        val message: String = ""
) {
        var isSuccess = status == "OK" && message == "success"
}