package com.tokopedia.sessioncommon.data.register

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class RegisterV2Param(
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

    @SerializedName("h")
    val hash: String = ""
) : GqlParam
