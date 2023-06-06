package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

sealed class RegisterProgressiveResult(
    val status: Int = 0,
    val challengeId: String = "",
    val throwable: Throwable? = null,
    val rejectionReason: String = ""
) {
    class Loading : RegisterProgressiveResult()
    class RiskyUser(challengeId: String) : RegisterProgressiveResult(challengeId = challengeId)
    class NotRiskyUser(status: Int, rejectionReason: String = "") : RegisterProgressiveResult(status = status, rejectionReason = rejectionReason)
    class Failed(throwable: Throwable) : RegisterProgressiveResult(throwable = throwable)
}
