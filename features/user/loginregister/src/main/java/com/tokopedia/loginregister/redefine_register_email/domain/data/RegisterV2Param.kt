package com.tokopedia.loginregister.redefine_register_email.domain.data

import com.google.gson.annotations.SerializedName

data class RegisterV2Param (
    @SerializedName("reg_type")
    val regType: String = "",

    @SerializedName("fullname")
    val fullName: String = "",

    @SerializedName("email")
    val email: String = "",

    @SerializedName("phone")
    val phone: String = "",

    @SerializedName("password")
    val password: String = "",

    @SerializedName("validate_token")
    val validateToken: String = "",

    @SerializedName("captcha_token")
    val captchaToken: String = "",

    @SerializedName("h")
    val h: String = ""
)