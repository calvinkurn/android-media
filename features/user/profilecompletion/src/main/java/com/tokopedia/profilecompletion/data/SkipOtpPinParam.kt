package com.tokopedia.profilecompletion.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class SkipOtpPinParam(
    @SerializedName("otpType")
    val otpType: Int = 0,

    @SerializedName("validateToken")
    val validateToken: String = ""
): GqlParam
