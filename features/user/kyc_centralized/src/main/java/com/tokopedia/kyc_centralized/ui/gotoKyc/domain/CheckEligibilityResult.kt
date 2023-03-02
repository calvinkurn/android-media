package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

sealed class CheckEligibilityResult(
    val encryptedName: String = "",
    val throwable: Throwable? = null
) {
    class Progressive(encryptedName: String): CheckEligibilityResult(encryptedName = encryptedName)
    class NonProgressive(): CheckEligibilityResult()
    class Failed(throwable: Throwable): CheckEligibilityResult(throwable = throwable)
}
