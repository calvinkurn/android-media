package com.tokopedia.otp.notif.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 22/09/20.
 */

data class VerifyPushNotifPojo(
        @SerializedName("VerifyPushnotif")
        @Expose
        var data: VerifyPushNotifData = VerifyPushNotifData()
)

data class VerifyPushNotifData(
        @SerializedName("success")
        @Expose
        var success: Boolean = false,
        @SerializedName("message")
        @Expose
        var message: String = "",
        @SerializedName("errorMessage")
        @Expose
        var errorMessage: String = "",
        @SerializedName("Imglink")
        @Expose
        var imglink: String = "",
        @SerializedName("MessageTitle")
        @Expose
        var messageTitle: String = "",
        @SerializedName("MessageBody")
        @Expose
        var messageBody: String = "",
        @SerializedName("CtaType")
        @Expose
        var ctaType: String = "",
        @SerializedName("Status")
        @Expose
        var status: String = ""
)