package com.tokopedia.loginregister.redefine_register_email.input_phone.domain.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class UserProfileUpdateParam(

    @SerializedName("phone")
    val phone: String = "",

    @SerializedName("currValidateToken")
    val currValidateToken: String = ""

) : GqlParam