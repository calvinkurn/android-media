package com.tokopedia.loginregister.login_sdk.data

import com.google.gson.annotations.SerializedName

data class SdkAuthorizeResponse (
    @SerializedName("oauth_authorize")
    val data: AuthorizeData
)

data class AuthorizeData(
    @SerializedName("code")
    val code: String = "",
    @SerializedName("expires_in")
    val expires: Int = 0,
    @SerializedName("is_success")
    val isSuccess: Boolean = false,
    @SerializedName("redirect_uri")
    val redirectUri: String,
    @SerializedName("error")
    val error: String = ""
)
