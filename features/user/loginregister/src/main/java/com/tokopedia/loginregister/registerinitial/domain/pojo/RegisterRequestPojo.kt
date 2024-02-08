package com.tokopedia.loginregister.registerinitial.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Deprecated("move use RegisterRequestV2")
data class RegisterRequestPojo(
        @SerializedName("register")
        @Expose
        var data: RegisterRequestData = RegisterRequestData()
)

