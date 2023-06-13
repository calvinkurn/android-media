package com.tokopedia.loginregister.login.domain.model

import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckFingerprintResult

data class LoginOption(
    val isEnableSeamless: Boolean,
    val isEnableBiometrics: Boolean,
    val biometricsData: RegisterCheckFingerprintResult
)
