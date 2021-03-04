package com.tokopedia.notifications.data.model

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("cmAddToken")
    val cmAddToken: CmAddToken
)

data class CmAddToken(
    @SerializedName("Error")
    val error: String?,
    @SerializedName("UserId")
    val userId: Int?
)