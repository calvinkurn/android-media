package com.tokopedia.otp.notif.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 22/09/20.
 */

data class ChangeStatusPushNotifPojo(
        @SerializedName("ChangeStatusPushNotif")
        @Expose
        var data: ChangeStatusPushNotifData = ChangeStatusPushNotifData()
)

data class ChangeStatusPushNotifData(
        @SerializedName("success")
        @Expose
        var success: Boolean = false,
        @SerializedName("errorMessage")
        @Expose
        var errorMessage: String = "",
        var isChecked: Boolean = false
)
