package com.tokopedia.tokopatch.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @Expose
    @SerializedName("id_token")
    val token: String = "",
    @Expose
    @SerializedName("error")
    val error: String = "",
    @Expose
    @SerializedName("error_description")
    val errorDescription: String = ""
)