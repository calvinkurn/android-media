package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

sealed class SubmitChallengeResult(
    val message: String = "",
    val throwable: Throwable? = null
) {
    class Loading: SubmitChallengeResult()
    class Success: SubmitChallengeResult()
    class WrongAnswer(message: String): SubmitChallengeResult(message = message)
    class Exhausted: SubmitChallengeResult()
    class Failed(throwable: Throwable): SubmitChallengeResult(throwable = throwable)
}
