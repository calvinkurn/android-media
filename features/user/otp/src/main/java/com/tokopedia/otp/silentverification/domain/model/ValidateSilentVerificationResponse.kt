package com.tokopedia.otp.silentverification.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yoris on 17/10/21.
 */

data class ValidateSilentVerificationResponse(
    @SerializedName("validateSilentVerification")
    @Expose
    var data: ValidateSilentVerificationResult = ValidateSilentVerificationResult()
)

data class ValidateSilentVerificationResult(
    @SerializedName("is_verified")
    @Expose
    var isVerified: Boolean = false
)