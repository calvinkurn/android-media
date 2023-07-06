package com.tokopedia.loginregister.login.domain.model

data class LoginOption(
    val isEnableSeamless: Boolean,
    val isEnableBiometrics: Boolean,
    val isEnableOcl: Boolean,
    val isEnableDirectBiometric: Boolean
)
