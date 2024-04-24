package com.tokopedia.loginregister.login_sdk.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class SdkAuthorizeParam(
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("redirect_uri")
    val redirectUri: String,
    @SerializedName("response_type")
    var responseType: String = "code",
    @SerializedName("code_challenge")
    var codeChallenge: String,
    @SerializedName("code_challenge_method")
    var codeChallengeMethod: String = "S256"
): GqlParam

data class SdkAuthorizeInput(
    @SerializedName("input")
    val input: SdkAuthorizeParam
): GqlParam
