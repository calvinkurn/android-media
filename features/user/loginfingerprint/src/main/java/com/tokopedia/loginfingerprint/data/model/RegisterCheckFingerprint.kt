package com.tokopedia.loginfingerprint.data.model

import com.google.gson.annotations.SerializedName

data class RegisterCheckFingerprint(
    @SerializedName("OTPBiometricCheckRegistered")
    var data: CheckFingerprintResult = CheckFingerprintResult()
)

data class RegisterCheckFingerprintResult(
    @SerializedName("Registered")
    var isRegistered: Boolean = false,

    @SerializedName("errorMessage")
    var errorMessage: String = ""
)