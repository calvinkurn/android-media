package com.tokopedia.sessioncommon.data.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class FingerPrintGqlParam(
    @SerializedName("grant_type")
    val grantType: String,

    @SerializedName("social_type")
    val socialType: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("validate_token")
    val validateToken: String,

    @SerializedName("device_biometrics")
    val deviceBiometrics: String
) : GqlParam
