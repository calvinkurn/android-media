package com.tokopedia.settingnotif.usersetting.data.pojo


import com.google.gson.annotations.SerializedName

data class UserSetting(
        @SerializedName(value = "", alternate = [
                "pushnotif",
                "email",
                "sms",
                "sellernotif"
        ])
        var settingSections: List<SettingSections?> = emptyList()
)