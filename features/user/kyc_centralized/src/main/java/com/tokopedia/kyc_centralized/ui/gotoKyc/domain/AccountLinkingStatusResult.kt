package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

sealed class AccountLinkingStatusResult(
    val throwable: Throwable? = null
) {
    class Loading : AccountLinkingStatusResult()
    class Linked : AccountLinkingStatusResult()
    class NotLinked : AccountLinkingStatusResult()
    class Failed(throwable: Throwable) : AccountLinkingStatusResult(throwable = throwable)
}
