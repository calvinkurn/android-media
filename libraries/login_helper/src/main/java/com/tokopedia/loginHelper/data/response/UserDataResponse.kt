package com.tokopedia.loginHelper.data.response

import com.google.gson.annotations.SerializedName

data class UserDataResponse(
    @SerializedName("email")
    val email: String?,
    @SerializedName("password")
    val password: String?,
    @SerializedName("tribe")
    val tribe: String? = null,
    @SerializedName("id")
    val id: Long? = 0
)
