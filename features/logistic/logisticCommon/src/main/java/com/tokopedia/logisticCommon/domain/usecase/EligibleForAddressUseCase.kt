package com.tokopedia.logisticCommon.domain.usecase

import com.tokopedia.logisticCommon.data.repository.KeroRepository
import com.tokopedia.logisticCommon.data.response.EligibleForAddressFeatureResponse
import com.tokopedia.logisticCommon.data.response.KeroAddrIsEligibleForAddressFeature
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class EligibleForAddressUseCase @Inject constructor(private val repository: KeroRepository) : UseCase<EligibleForAddressFeatureResponse>() {

    private var featureId = 0

    fun eligibleForAddressFeature(success: (KeroAddrIsEligibleForAddressFeature) -> Unit, onFail: (Throwable) -> Unit, featureId: Int) {
        execute({
            success(it.data)
        }, {
            onFail(it)
        })

        this.featureId = featureId
    }

    override suspend fun executeOnBackground(): EligibleForAddressFeatureResponse {
        return repository.eligibleForAddressFeature(featureId)
    }

}