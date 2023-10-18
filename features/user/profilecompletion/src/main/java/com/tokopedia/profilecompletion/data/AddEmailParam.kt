package com.tokopedia.profilecompletion.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class AddEmailParam (
    @SerializedName("email")
    val email: String = "",

    @SerializedName("otpCode")
    val otpCode: String = "",

    @SerializedName("validate_token")
    val validateToken: String = ""
) : GqlParam
