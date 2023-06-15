package com.tokopedia.loginHelper.data.response

import com.google.gson.annotations.SerializedName

data class LoginHelperDeleteUserResponse(
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("code")
    val code: Long
)
