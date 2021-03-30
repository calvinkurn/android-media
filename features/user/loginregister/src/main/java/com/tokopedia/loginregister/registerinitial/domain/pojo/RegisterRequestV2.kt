package com.tokopedia.loginregister.registerinitial.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterRequestV2 (
        @SerializedName("register_v2")
        @Expose
        var data: RegisterRequestData = RegisterRequestData()
)
