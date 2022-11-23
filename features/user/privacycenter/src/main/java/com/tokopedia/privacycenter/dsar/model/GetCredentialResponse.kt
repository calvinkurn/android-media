package com.tokopedia.privacycenter.dsar.model

import com.google.gson.annotations.SerializedName

data class GetCredentialResponse(
    @SerializedName("access_token")
    val accessToken: String = "",
    @SerializedName("token_type")
    val tokenType: String = ""
)
