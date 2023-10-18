package com.tokopedia.sessioncommon.data.ocl

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class LoginOclParam(
    @SerializedName("ocl_jwt_token")
    val oclToken: String,
    @SerializedName("access_token")
    val accountToken: String,
    @SerializedName("grant_type")
    val grantType: String = "extension",
    @SerializedName("social_type")
    val socialType: String = "10"
) : GqlParam
