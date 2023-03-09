package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

sealed class SubmitChallengeResult(
    val throwable: Throwable? = null
) {
    class Loading: SubmitChallengeResult()
    class Success: SubmitChallengeResult()
    class Failed(throwable: Throwable): SubmitChallengeResult(throwable = throwable)
}
