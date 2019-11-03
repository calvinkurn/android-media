package com.tokopedia.scanner.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VerificationResponse(
    @SerializedName("url")
    @Expose
    val url: String = ""
)