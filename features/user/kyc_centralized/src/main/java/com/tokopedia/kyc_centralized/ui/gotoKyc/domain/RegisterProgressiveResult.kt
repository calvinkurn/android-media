package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

sealed class RegisterProgressiveResult(
    val isPending: Boolean = false,
    val challengeId: String = "",
    val throwable: Throwable? = null
) {
    class Loading : RegisterProgressiveResult()
    class RiskyUser(challengeId: String) : RegisterProgressiveResult(challengeId = challengeId)
    class NotRiskyUser(isPending: Boolean) : RegisterProgressiveResult(isPending = isPending)
    class Failed(throwable: Throwable) : RegisterProgressiveResult(throwable = throwable)
}
