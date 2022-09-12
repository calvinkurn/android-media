package com.tokopedia.loginregister.redefine_register_email.view.input_phone.domain.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class UserProfileValidateParam(

    @SerializedName("email")
    val email: String = "",

    @SerializedName("phone")
    val phone: String = ""

) : GqlParam