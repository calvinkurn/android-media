package com.tokopedia.loginregister.goto_seamless.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetNameParam(
    @SerializedName("code")
    val authCode: String
): GqlParam
