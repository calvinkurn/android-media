package com.tokopedia.interceptors.refreshtoken

import com.google.gson.annotations.SerializedName

/**
 * Created by Yoris on 13/10/21.
 */

data class RefreshTokenModel(
    @SerializedName("login_token")
    var loginToken: RefreshToken = RefreshToken()
)


data class RefreshToken(
    @SerializedName("acc_sid")
    var accountSid: String = "",
    @SerializedName("access_token")
    var accessToken: String = "",
    @SerializedName("refresh_token")
    var refreshToken: String = "",
    @SerializedName("expires_in")
    var expiresIn: Int = 0,
    @SerializedName("token_type")
    var tokenType: String = "",
    @SerializedName("sq_check")
    var sqCheck: Boolean = false,
    @SerializedName("event_code")
    var eventCode: String = "",
    @SerializedName("action")
    var action: Int = 0,
    @SerializedName("errors")
    var errors: ArrayList<Error> = arrayListOf()
)