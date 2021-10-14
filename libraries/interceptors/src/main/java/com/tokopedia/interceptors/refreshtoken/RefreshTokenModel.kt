package com.tokopedia.interceptors.refreshtoken

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yoris on 13/10/21.
 */

data class RefreshTokenModel(
    @SerializedName("login_token")
    @Expose
    var loginToken: RefreshToken = RefreshToken()
)


data class RefreshToken(
    @SerializedName("acc_sid")
    @Expose
    var accountSid: String = "",
    @SerializedName("access_token")
    @Expose
    var accessToken: String = "",
    @SerializedName("refresh_token")
    @Expose
    var refreshToken: String = "",
    @SerializedName("expires_in")
    @Expose
    var expiresIn: Int = 0,
    @SerializedName("token_type")
    @Expose
    var tokenType: String = "",
    @SerializedName("sq_check")
    @Expose
    var sqCheck: Boolean = false,
    @SerializedName("event_code")
    @Expose
    var eventCode: String = "",
    @SerializedName("action")
    @Expose
    var action: Int = 0,
    @SerializedName("errors")
    @Expose
    var errors: ArrayList<Error> = arrayListOf()
)