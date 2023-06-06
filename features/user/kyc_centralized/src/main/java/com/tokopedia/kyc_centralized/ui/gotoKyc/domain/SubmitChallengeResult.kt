package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

sealed class SubmitChallengeResult(
    val attemptsRemaining: String = "",
    val maximumAttemptsAllowed: String = "",
    val cooldownTimeInSeconds: String = "",
    val throwable: Throwable? = null
) {
    class Loading: SubmitChallengeResult()
    class Success: SubmitChallengeResult()
    class WrongAnswer(
        attemptsRemaining: String
    ): SubmitChallengeResult(
        attemptsRemaining = attemptsRemaining
    )
    class Exhausted(cooldownTimeInSeconds: String, maximumAttemptsAllowed: String): SubmitChallengeResult(
        maximumAttemptsAllowed = maximumAttemptsAllowed,
        cooldownTimeInSeconds = cooldownTimeInSeconds
    )
    class Failed(throwable: Throwable): SubmitChallengeResult(throwable = throwable)
}
