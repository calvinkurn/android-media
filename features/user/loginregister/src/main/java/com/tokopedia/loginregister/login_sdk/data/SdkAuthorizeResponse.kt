package com.tokopedia.loginregister.login_sdk.data

import com.google.gson.annotations.SerializedName

data class SdkAuthorizeResponse (
    @SerializedName("oauth_authorize")
    val data: AuthorizeData
)

data class AuthorizeData(
    @SerializedName("code")
    val code: String,
    @SerializedName("expires_in")
    val expires: Int,
    @SerializedName("is_success")
    val isSuccess: Boolean,
    @SerializedName("redirect_uri")
    var redirectUri: String,
    @SerializedName("error")
    var error: String = ""
)
