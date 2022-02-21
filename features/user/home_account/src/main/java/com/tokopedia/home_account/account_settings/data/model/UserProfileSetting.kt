package com.tokopedia.home_account.account_settings.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserProfileSetting(
        @SerializedName("safeMode")
        @Expose
        val safeMode: Boolean
)