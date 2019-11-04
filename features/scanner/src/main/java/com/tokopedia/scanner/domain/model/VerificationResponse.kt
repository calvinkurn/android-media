package com.tokopedia.scanner.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VerificationResponse(
    @SerializedName("triggerVerifyQR")
    @Expose
    val data: VerifyQrData = VerifyQrData()
) {
    data class VerifyQrData(
        @SerializedName("url")
        @Expose
        val url: String = ""
    )
}