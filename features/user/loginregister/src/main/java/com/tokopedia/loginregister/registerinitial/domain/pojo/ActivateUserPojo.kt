package com.tokopedia.loginregister.registerinitial.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-10-21.
 * ade.hadian@tokopedia.com
 */

data class ActivateUserPojo(
        @SerializedName("is_success")
        @Expose
        var isSuccess: Boolean = false,
        @SerializedName("message")
        @Expose
        var message: String = "",
        @SerializedName("sid")
        @Expose
        var sid: String = "",
        @SerializedName("access_token")
        @Expose
        var accessToken: String = "",
        @SerializedName("refresh_token")
        @Expose
        var refreshToken: String = "",
        @SerializedName("token_type")
        @Expose
        var tokenType: String = ""
)