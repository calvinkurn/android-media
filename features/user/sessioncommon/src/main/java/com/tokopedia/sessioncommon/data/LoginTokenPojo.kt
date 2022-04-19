package com.tokopedia.sessioncommon.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 28/05/19.
 */

data class LoginTokenPojo(
        @SerializedName("login_token")
        @Expose
        var loginToken: LoginToken = LoginToken()
)


data class LoginToken(
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
        var errors: ArrayList<Error> = arrayListOf(),
        @SerializedName("popup_error")
        @Expose
        var popupError: PopupError = PopupError()
)

data class PopupError (
        @SerializedName("header")
        @Expose
        var header: String = "",
        @SerializedName("body")
        @Expose
        var body: String = "",
        @SerializedName("action")
        @Expose
        var action: String = ""
)

