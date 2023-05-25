package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

import com.gojek.kyc.sdk.core.utils.KycSdkPartner
import com.gojek.OneKycSdk
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.network.exception.MessageErrorException
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CheckEligibilityUseCase @Inject constructor(
    private val oneKycSdk: OneKycSdk,
    private val dispatchers: CoroutineDispatchers
) {
    suspend fun invoke(): CheckEligibilityResult {
        return withContext(dispatchers.io) {
            val response = oneKycSdk.getOneKycNetworkRepository().getEligibilityStatus(partnerName = KycSdkPartner.TOKOPEDIA_CORE.name)

            if (response.success == false) {
                CheckEligibilityResult.Failed(MessageErrorException(response.errors?.first()?.message.orEmpty()))
            } else {
                when (response.data?.flow) {
                    KYCConstant.GotoKycFlow.PROGRESSIVE -> {
                        CheckEligibilityResult.Progressive(response.data?.details?.maskedName.orEmpty())
                    }
                    KYCConstant.GotoKycFlow.NON_PROGRESSIVE -> {
                        CheckEligibilityResult.NonProgressive()
                    }
                    KYCConstant.GotoKycFlow.AWAITING_APPROVAL -> {
                        CheckEligibilityResult.AwaitingApprovalGopay()
                    }
                    else -> {
                        CheckEligibilityResult.Failed(Throwable(message = response.data?.flow.toString()))
                    }
                }
            }
        }
    }
}
