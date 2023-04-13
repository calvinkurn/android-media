package com.tokopedia.profilecompletion.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class ResetPin2FAParam(
    @SerializedName("user_id")
    val userId: Int = 0,

    @SerializedName("validate_token")
    val validateToken: String = "",

    @SerializedName("grant_type")
    val grantType: String = ""
): GqlParam
