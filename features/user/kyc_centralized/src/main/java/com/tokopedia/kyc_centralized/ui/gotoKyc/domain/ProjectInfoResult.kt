package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

import com.tokopedia.kyc_centralized.ui.gotoKyc.main.StatusSubmissionParam

sealed class ProjectInfoResult(
    val status: String = "",
    val dataSource: String = "",
    val listReason: List<String> = emptyList(),
    val isAccountLinked: Boolean? = null,
    val throwable: Throwable? = null
) {
    class TokoKyc() : ProjectInfoResult()
    class StatusSubmission(status: String, dataSource: String, listReason: List<String>) : ProjectInfoResult(
        status = status,
        dataSource = dataSource,
        listReason = listReason
    )
    class NotVerified(isAccountLinked: Boolean) : ProjectInfoResult(
        isAccountLinked = isAccountLinked
    )
    class Failed(throwable: Throwable) : ProjectInfoResult(throwable = throwable)
}
