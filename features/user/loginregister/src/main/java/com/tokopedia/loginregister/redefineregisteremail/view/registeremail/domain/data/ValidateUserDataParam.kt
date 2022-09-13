package com.tokopedia.loginregister.redefineregisteremail.view.registeremail.domain.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

class ValidateUserDataParam(

    @SerializedName("email")
    val email: String = "",

    @SerializedName("fullname")
    val fullName: String = "",

    @SerializedName("password")
    val password: String = "",

    @SerializedName("h")
    val hash: String = ""

): GqlParam