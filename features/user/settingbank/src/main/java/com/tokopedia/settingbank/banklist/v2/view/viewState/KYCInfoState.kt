package com.tokopedia.settingbank.banklist.v2.view.viewState

import com.tokopedia.settingbank.banklist.v2.domain.KYCInfo

sealed class KYCInfoState
object KYCInfoRequestStarted : KYCInfoState()
object KYCInfoRequestEnded : KYCInfoState()
data class OnKYCInfoResponse(val kycInfo: KYCInfo) : KYCInfoState()
data class KYCInfoError(val message: String) : KYCInfoState()
object KYCNetworkError : KYCInfoState()