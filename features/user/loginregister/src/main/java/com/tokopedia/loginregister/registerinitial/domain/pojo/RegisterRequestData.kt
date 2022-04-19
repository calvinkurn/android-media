package com.tokopedia.loginregister.registerinitial.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterRequestData(
        @SerializedName("user_id")
        @Expose
        var userId: String = "0",
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
        var tokenType: String = "",
        @SerializedName("h")
        @Expose
        var hash: String = "",
        @SerializedName("is_active")
        @Expose
        var isActive: Int = 0,
        @SerializedName("action")
        @Expose
        var action: Int = 0,
        @SerializedName("errors")
        @Expose
        var errors: ArrayList<RegisterRequestErrorData> = arrayListOf()
)
