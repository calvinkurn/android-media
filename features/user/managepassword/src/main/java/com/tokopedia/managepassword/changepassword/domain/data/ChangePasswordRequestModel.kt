package com.tokopedia.managepassword.changepassword.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChangePasswordRequestModel(
        @Expose
        @SerializedName("encode")
        val encode: String = "encode",
        @Expose
        @SerializedName("new_password")
        val newPassword: String = "",
        @Expose
        @SerializedName("repeat_password")
        val repeatPassword: String = "",
        @Expose
        @SerializedName("validatetoken")
        val validateToken: String = "token"
)