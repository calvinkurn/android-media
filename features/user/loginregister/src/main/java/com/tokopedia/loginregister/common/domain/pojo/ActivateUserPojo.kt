package com.tokopedia.loginregister.common.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-10-21.
 * ade.hadian@tokopedia.com
 */

data class ActivateUserPojo(
        @SerializedName("activate_user")
        @Expose
        var data: ActivateUserData = ActivateUserData()
)

data class ActivateUserData(
        @SerializedName("is_success")
        @Expose
        var isSuccess: Int = 0,
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