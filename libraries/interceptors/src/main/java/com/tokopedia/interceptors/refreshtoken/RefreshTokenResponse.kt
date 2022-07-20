package com.tokopedia.interceptors.refreshtoken

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(
    @SerializedName("login_token")
    var loginToken: RefreshTokenData? = RefreshTokenData()
)

data class RefreshTokenData(
    @SerializedName("acc_sid")
    var accountSid: String? = "",
    @SerializedName("access_token")
    var accessToken: String? = "",
    @SerializedName("refresh_token")
    var refreshToken: String? = "",
    @SerializedName("expires_in")
    var expiresIn: Int? = 0,
    @SerializedName("token_type")
    var tokenType: String? = "",
    @SerializedName("sq_check")
    var sqCheck: Boolean? = false,
    @SerializedName("event_code")
    var eventCode: String? = "",
    @SerializedName("action")
    var action: Int? = 0,
    @SerializedName("errors")
    var errors: ArrayList<Error>? = arrayListOf()
)

data class Error(
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("message")
    var message: String? = ""
)