package com.tokopedia.loginfingerprint.data.model

import com.google.gson.annotations.SerializedName

data class CheckFingerprintPojo(
    @SerializedName("OTPBiometricCheckToggleStatus")
    var data: CheckFingerprintResult = CheckFingerprintResult()
)

data class CheckFingerprintResult(
    @SerializedName("is_active")
    var isRegistered: Boolean = false,

    @SerializedName("is_success")
    var isSuccess: Boolean = false,

    @SerializedName("error_message")
    var errorMessage: String = ""
)