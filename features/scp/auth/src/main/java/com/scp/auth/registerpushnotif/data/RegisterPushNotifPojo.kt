package com.scp.auth.registerpushnotif.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 17/09/20.
 */

data class RegisterPushNotifPojo(
        @SerializedName("RegisterPushnotif")
        @Expose
        var data: RegisterPushNotifData = RegisterPushNotifData()
)

data class RegisterPushNotifData(
        @SerializedName("success")
        @Expose
        var isSuccess: Boolean = false,
        @SerializedName("errorMessage")
        @Expose
        var errorMessage: String = "",
        @SerializedName("message")
        @Expose
        var message: String = ""
)
