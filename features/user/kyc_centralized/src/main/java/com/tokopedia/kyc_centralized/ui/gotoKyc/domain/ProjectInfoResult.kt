package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

sealed class ProjectInfoResult(
    val status: String = "",
    val rejectionReason: String = "",
    val isAccountLinked: Boolean = false,
    val waitMessage: String = "",
    val throwable: Throwable? = null
) {
    class TokoKyc() : ProjectInfoResult()
    class StatusSubmission(status: String, rejectionReason: String, waitMessage: String) : ProjectInfoResult(
        status = status,
        rejectionReason = rejectionReason,
        waitMessage = waitMessage
    )
    class NotVerified(isAccountLinked: Boolean) : ProjectInfoResult(
        isAccountLinked = isAccountLinked
    )
    class Failed(throwable: Throwable) : ProjectInfoResult(throwable = throwable)
}
