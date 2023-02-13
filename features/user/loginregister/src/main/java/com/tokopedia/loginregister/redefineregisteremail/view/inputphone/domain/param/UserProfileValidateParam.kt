package com.tokopedia.loginregister.redefineregisteremail.view.inputphone.domain.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class UserProfileValidateParam(

    @SerializedName("email")
    val email: String = "",

    @SerializedName("phone")
    val phone: String = ""

) : GqlParam