package com.tokopedia.home_account.data.model

import com.google.gson.annotations.SerializedName

data class SetUserProfileSettingResponse(
        @SerializedName("userProfileSettingUpdate")
        val userProfileSettingUpdate: SetUserProfileSetting = SetUserProfileSetting()
)
