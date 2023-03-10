package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

sealed class KycStatusResult(
    val status: String = "",
    val throwable: Throwable? = null
) {
    class Loading: KycStatusResult()
    class Success(status: String): KycStatusResult(status = status)
    class Failed(throwable: Throwable): KycStatusResult(throwable = throwable)
}
