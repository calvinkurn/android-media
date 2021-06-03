package com.tokopedia.loginfingerprint.data.model

import com.google.gson.annotations.SerializedName

data class CheckFingerprintPojo(
    @SerializedName("OTPBiometricCheckRegistered")
    var data: CheckFingerprintResult = CheckFingerprintResult()
)

data class CheckFingerprintResult(
    @SerializedName("Registered")
    var isRegistered: Boolean = false,

    @SerializedName("errorMessage")
    var errorMessage: String = ""
)