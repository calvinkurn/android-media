package com.tokopedia.settingnotif.usersetting.domain.pojo


import com.google.gson.annotations.SerializedName

data class UserSetting(
        @SerializedName(value = "", alternate = ["pushnotif", "email", "sms"])
        var settingSections: List<SettingSections?> = emptyList()
)