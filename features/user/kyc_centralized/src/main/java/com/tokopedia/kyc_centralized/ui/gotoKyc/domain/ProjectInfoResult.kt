package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

sealed class ProjectInfoResult(
    val status: String = "",
    val listReason: List<String> = emptyList(),
    val isAccountLinked: Boolean = false,
    val waitTimeInSeconds: Int = 0,
    val throwable: Throwable? = null
) {
    class TokoKyc() : ProjectInfoResult()
    class StatusSubmission(status: String, listReason: List<String>, waitTimeInSeconds: Int) : ProjectInfoResult(
        status = status,
        listReason = listReason,
        waitTimeInSeconds = waitTimeInSeconds
    )
    class NotVerified(isAccountLinked: Boolean) : ProjectInfoResult(
        isAccountLinked = isAccountLinked
    )
    class Failed(throwable: Throwable) : ProjectInfoResult(throwable = throwable)
}
