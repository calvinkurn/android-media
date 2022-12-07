package com.tokopedia.privacycenter.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class AddEmailParam(
    @SerializedName("email")
    val email: String,
    @SerializedName("otpCode")
    val otpCode: String,
    @SerializedName("validateToken")
    val validateToken: String
) : GqlParam
