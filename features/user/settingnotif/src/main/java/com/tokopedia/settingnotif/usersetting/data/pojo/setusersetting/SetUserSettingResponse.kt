package com.tokopedia.settingnotif.usersetting.data.pojo.setusersetting


import com.google.gson.annotations.SerializedName

data class SetUserSettingResponse(
        @SerializedName("notifier_setUserSpecificSettings")
        val setUserSettingStatus: SetUserSettingStatus?
)