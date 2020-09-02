package com.tokopedia.settingbank.view.viewState

import com.tokopedia.settingbank.domain.KYCInfo

sealed class KYCInfoState
object KYCInfoRequestStarted : KYCInfoState()
object KYCInfoRequestEnded : KYCInfoState()
data class OnKYCInfoResponse(val kycInfo: KYCInfo) : KYCInfoState()
data class KYCInfoError(val throwable: Throwable) : KYCInfoState()