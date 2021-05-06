package com.tokopedia.loginregister.registerinitial.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterRequestErrorData(
        @SerializedName("name")
        @Expose
        var name: String = "",
        @SerializedName("message")
        @Expose
        var message: String = ""
)
