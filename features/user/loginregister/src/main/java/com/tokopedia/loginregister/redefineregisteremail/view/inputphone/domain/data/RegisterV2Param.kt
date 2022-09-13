package com.tokopedia.loginregister.redefineregisteremail.view.inputphone.domain.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class RegisterV2Param(
    @SerializedName("reg_type")
    val regType: String = "",

    @SerializedName("os_type")
    val osType: String = "",

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

    @SerializedName("h")
    val h: String = ""
) : GqlParam