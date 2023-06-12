package com.tokopedia.loginHelper.data.body

import com.google.gson.annotations.SerializedName

data class LoginHelperAddUserBody(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("tribe")
    val tribe: String? = null
)
