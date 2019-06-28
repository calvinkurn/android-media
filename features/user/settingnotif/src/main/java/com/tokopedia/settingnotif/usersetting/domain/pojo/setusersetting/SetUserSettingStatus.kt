package com.tokopedia.settingnotif.usersetting.domain.pojo.setusersetting


import com.google.gson.annotations.SerializedName

data class SetUserSettingStatus(
        @SerializedName("error_message")
        val errorMessage: String = "",
        @SerializedName("is_success")
        val isSuccess: Int = 0
)