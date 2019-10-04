package com.tokopedia.home.account.data.model

import com.google.gson.annotations.SerializedName

data class SetUserProfileSetting(@SerializedName("isSuccess")
                                 val isSuccess: Boolean,
                                 @SerializedName("error")
                                 val error: String)