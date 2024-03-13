package com.tokopedia.loginregister.login_sdk.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class SdkConsentParam(
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("scopes")
    val scopes: String,
    @SerializedName("lang")
    var lang: String = ""
): GqlParam
