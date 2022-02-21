package com.tokopedia.home_account.account_settings.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserProfileSettingResponse(
        @SerializedName("userProfileSetting")
        @Expose
        val userProfileSetting: UserProfileSetting
)