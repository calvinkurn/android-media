package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

sealed class ProjectInfoResult(
    isAccountLinked: Boolean? = null,
    isKtpAlreadyTaken: Boolean? = null,
    throwable: Throwable? = null
) {
    class NotGoToKyc() : ProjectInfoResult()
    class StatusSubmission() : ProjectInfoResult()
    class Progressive() : ProjectInfoResult()
    class NonProgressive(isAccountLinked: Boolean, isKtpAlreadyTaken: Boolean) : ProjectInfoResult(
        isAccountLinked = isAccountLinked, isKtpAlreadyTaken = isKtpAlreadyTaken
    )
    class Failed(throwable: Throwable) : ProjectInfoResult(throwable = throwable)
}
