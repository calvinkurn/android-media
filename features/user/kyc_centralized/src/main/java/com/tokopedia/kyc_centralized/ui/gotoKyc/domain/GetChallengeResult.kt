package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

sealed class GetChallengeResult(
    val questionId: String = "",
    val throwable: Throwable? = null
) {
    class Loading(): GetChallengeResult()
    class Success(questionId: String): GetChallengeResult(questionId = questionId)
    class Failed(throwable: Throwable): GetChallengeResult(throwable = throwable)
}
