package com.tokopedia.managepassword.forgotpassword.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ForgotPasswordRequestModel(
        @Expose
        @SerializedName("emailphone")
        val emailOrPhoneNumber: String = ""
)