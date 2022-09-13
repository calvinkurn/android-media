package com.tokopedia.loginregister.redefineregisteremail.view.inputphone.domain.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class UserProfileUpdateParam(

    @SerializedName("phone")
    val phone: String = "",

    @SerializedName("currValidateToken")
    val currentValidateToken: String = ""

) : GqlParam