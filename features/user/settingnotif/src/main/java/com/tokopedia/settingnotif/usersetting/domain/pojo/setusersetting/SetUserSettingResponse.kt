package com.tokopedia.settingnotif.usersetting.domain.pojo.setusersetting


import com.google.gson.annotations.SerializedName

data class SetUserSettingResponse(
        @SerializedName("notifier_setUserSpecificSettings")
        val setUserSettingStatus: SetUserSettingStatus?
)