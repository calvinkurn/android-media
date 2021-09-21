package com.tokopedia.loginregister.login.domain.pojo

import com.google.gson.annotations.SerializedName

data class RegisterCheckFingerprint(
    @SerializedName("OTPBiometricCheckRegistered")
    var data: RegisterCheckFingerprintResult = RegisterCheckFingerprintResult()
)

data class RegisterCheckFingerprintResult(
    @SerializedName("registered")
    var isRegistered: Boolean = false,

    @SerializedName("errorMessage")
    var errorMessage: String = ""
)