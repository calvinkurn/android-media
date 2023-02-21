package com.tokopedia.login_helper.data.response

import com.google.gson.annotations.SerializedName

data class UserDataResponse(
    @SerializedName("email")
    private val email: String?,
    @SerializedName("password")
    private val password: String?,
    @SerializedName("tribe")
    private val tribe: String?,

)
