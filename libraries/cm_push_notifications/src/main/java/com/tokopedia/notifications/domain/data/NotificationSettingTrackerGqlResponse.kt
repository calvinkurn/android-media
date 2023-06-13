package com.tokopedia.notifications.domain.data

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


@SuppressLint("Invalid Data Type")
data class NotificationSettingTrackerGqlResponse(
    @SerializedName("notifier_setUserSpecificSettings")
    @Expose
    val settingTrackerResponse: SettingTrackerResponse = SettingTrackerResponse()
)

data class SettingTrackerResponse(
    @SerializedName("is_success")
    @Expose
    val isSuccess: Int = 0,
    @SerializedName("error_message")
    @Expose
    val errorMessage: String? = ""
)

