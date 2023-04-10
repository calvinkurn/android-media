package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

import com.gojek.kyc.sdk.core.utils.KycSdkPartner
import com.gojek.onekyc.OneKycSdk
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
            val response = oneKycSdk.getOneKycNetworkRepository().getEligibilityStatus(partnerName = KycSdkPartner.TOKO.name)

            if (response.success == false) {
                CheckEligibilityResult.Failed(MessageErrorException(response.errors?.first()?.message.orEmpty()))
            } else if (response.data?.flow == KYCConstant.GotoKycFlow.PROGRESSIVE) {
                CheckEligibilityResult.Progressive(response.data?.details?.maskedName.orEmpty())
            } else if (response.data?.flow == KYCConstant.GotoKycFlow.NON_PROGRESSIVE){
                CheckEligibilityResult.NonProgressive()
            } else {
                CheckEligibilityResult.Failed(MessageErrorException(""))
            }
        }
    }
}
