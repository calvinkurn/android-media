package com.tokopedia.loginHelper.data.response

import com.google.gson.annotations.SerializedName

data class LoginHelperAddUserResponse(
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("code")
    val code: Long? = null,
    @SerializedName("data")
    val addUserData: LoginHelperAddUserData? = null
) {
    data class LoginHelperAddUserData(
        @SerializedName("email")
        val email: String? = null,
        @SerializedName("password")
        val password: String? = null,
        @SerializedName("tribe")
        val tribe: String? = null,
        @SerializedName("id")
        val id: String? = null
    )
}
