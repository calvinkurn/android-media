package com.tokopedia.home_account.account_settings.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SetUserProfileSetting(
    @SerializedName("isSuccess")
    @Expose
    val isSuccess: Boolean,

    @SerializedName("error")
    @Expose
    val error: String
)