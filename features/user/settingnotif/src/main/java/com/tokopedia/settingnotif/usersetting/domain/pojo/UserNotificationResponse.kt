package com.tokopedia.settingnotif.usersetting.domain.pojo


import com.google.gson.annotations.SerializedName

data class UserNotificationResponse(
        @SerializedName("notifier_notificationGetUserAllSettings")
        var userSetting: UserSetting = UserSetting(emptyList())
)