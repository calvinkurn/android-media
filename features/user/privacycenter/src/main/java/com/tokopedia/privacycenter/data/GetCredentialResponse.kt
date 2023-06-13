package com.tokopedia.privacycenter.data

import com.google.gson.annotations.SerializedName

data class GetCredentialResponse(
    @SerializedName("access_token")
    val accessToken: String = "",
    @SerializedName("token_type")
    val tokenType: String = ""
)
