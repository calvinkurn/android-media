package com.tokopedia.loginregister.login.domain.pojo

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
        var success: Boolean = false,
        @SerializedName("errorMessage")
        @Expose
        var errorMessage: String = "",
        @SerializedName("message")
        @Expose
        var message: String = ""
)