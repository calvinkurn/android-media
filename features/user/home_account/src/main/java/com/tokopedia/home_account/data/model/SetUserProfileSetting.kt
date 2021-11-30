package com.tokopedia.home_account.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SetUserProfileSetting(
    @SerializedName("isSuccess")
    @Expose
    val isSuccess: Boolean = false,

    @SerializedName("error")
    @Expose
    val error: String = ""
)