package com.tokopedia.sessioncommon.data.register

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.sessioncommon.data.Error

/**
 * @author by nisie on 30/04/19.
 */

data class RegisterPojo(
        @SerializedName("register")
        @Expose
        var register: RegisterInfo = RegisterInfo()
){}

data class RegisterInfo(
        @SerializedName("user_id")
        @Expose
        var userId: String = "",
        @SerializedName("sid")
        @Expose
        var sid: String = "",
        @SerializedName("access_token")
        @Expose
        var accessToken: String = "",
        @SerializedName("refresh_token")
        @Expose
        var refreshToken: String = "",
        @SerializedName("errors")
        @Expose
        var errors: ArrayList<Error> = arrayListOf()
        ){}