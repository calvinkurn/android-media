package com.tokopedia.otp.silentverification.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yoris on 17/10/21.
 */

data class RequestSilentVerificationResponse(
    @SerializedName("requestSilentVerification")
    @Expose
    var data: RequestSilentVerificationResult = RequestSilentVerificationResult()
)

data class RequestSilentVerificationResult(
    @SerializedName("evurl")
    @Expose
    var evUrl: String = "",
    @SerializedName("correlation_id")
    @Expose
    var correlationId: String = ""
)