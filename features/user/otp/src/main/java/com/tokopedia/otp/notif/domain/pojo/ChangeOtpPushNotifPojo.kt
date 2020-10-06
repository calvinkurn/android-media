package com.tokopedia.otp.notif.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.otp.verification.domain.data.ModeListData

/**
 * Created by Ade Fulki on 22/09/20.
 */

data class ChangeOtpPushNotifPojo(
        @SerializedName("ChangeStatusPushNotif")
        @Expose
        var data: ChangeOtpPushNotifData = ChangeOtpPushNotifData()
)

data class ChangeOtpPushNotifData(
        @SerializedName("success")
        @Expose
        var success: Boolean = false,
        @SerializedName("errorMessage")
        @Expose
        var errorMessage: String = "",
        var isChecked: Boolean = false
)
