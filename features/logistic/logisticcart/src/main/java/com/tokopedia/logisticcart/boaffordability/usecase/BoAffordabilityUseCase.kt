package com.tokopedia.logisticcart.boaffordability.usecase

import com.tokopedia.logisticcart.boaffordability.model.BoAffordabilityGqlResponse
import com.tokopedia.usecase.coroutines.UseCase

class BoAffordabilityUseCase : UseCase<BoAffordabilityGqlResponse>() {

    override suspend fun executeOnBackground(): BoAffordabilityGqlResponse {
        TODO("Not yet implemented")
    }
}