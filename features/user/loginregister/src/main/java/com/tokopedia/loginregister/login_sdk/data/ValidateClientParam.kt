package com.tokopedia.loginregister.login_sdk.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class ValidateClientParam(
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("app_signature")
    val signature: String,
    @SerializedName("package_name")
    var packageName: String,
    @SerializedName("redirect_uri")
    var redirectUri: String
): GqlParam
