package com.tokopedia.loginregister.registerinitial.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterRequestPojo(
        @SerializedName("register")
        @Expose
        var data: RegisterRequestData = RegisterRequestData()
)

