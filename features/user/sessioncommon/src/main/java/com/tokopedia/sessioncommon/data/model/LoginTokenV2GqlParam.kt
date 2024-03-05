package com.tokopedia.sessioncommon.data.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class LoginTokenV2GqlParam(
    @SerializedName("grant_type")
    private val grantType: String,

    @SerializedName("username")
    private val username: String,

    @SerializedName("password")
    private val password: String,

    @SerializedName("h")
    private val hash: String

) : GqlParam
