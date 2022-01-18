package com.tokopedia.logisticCommon.domain.usecase

import com.tokopedia.logisticCommon.data.repository.KeroRepository
import com.tokopedia.logisticCommon.data.response.KeroAddrIsEligibleForAddressFeatureResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class EligibleForAddressUseCase @Inject constructor(private val repository: KeroRepository) : UseCase<KeroAddrIsEligibleForAddressFeatureResponse>() {

    private var featureId = 0

    fun eligibleForAddressFeature(success: (KeroAddrIsEligibleForAddressFeatureResponse) -> Unit, onFail: (Throwable) -> Unit, featureId: Int) {
        execute({
            success(it)
        }, {
            onFail(it)
        })

        this.featureId = featureId
    }

    override suspend fun executeOnBackground(): KeroAddrIsEligibleForAddressFeatureResponse {
        return repository.eligibleForAddressFeature(featureId)
    }

}