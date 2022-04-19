package com.tokopedia.otp.notif.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 22/09/20.
 */

data class VerifyPushNotifExpPojo(
        @SerializedName("VerifyPushnotifExpiration")
        @Expose
        var data: VerifyPushNotifExpData = VerifyPushNotifExpData()
)

data class VerifyPushNotifExpData(
        @SerializedName("is_success")
        @Expose
        var success: Boolean = false,
        @SerializedName("is_expire")
        @Expose
        var isExpire: Boolean = false,
        @SerializedName("is_trusted")
        @Expose
        var isTrusted: Boolean = false,
        @SerializedName("error_message")
        @Expose
        var errorMessage: String = "",
        @SerializedName("image_link")
        @Expose
        var imglink: String = "",
        @SerializedName("title")
        @Expose
        var messageTitle: String = "",
        @SerializedName("description")
        @Expose
        var messageBody: String = "",
        @SerializedName("button_type")
        @Expose
        var ctaType: String = "",
        @SerializedName("Status")
        @Expose
        var status: String = ""
)