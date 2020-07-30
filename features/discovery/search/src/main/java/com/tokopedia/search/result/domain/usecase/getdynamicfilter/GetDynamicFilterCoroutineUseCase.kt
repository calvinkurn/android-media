package com.tokopedia.search.result.domain.usecase.getdynamicfilter

import com.tokopedia.discovery.common.coroutines.Repository
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.usecase.coroutines.UseCase

class GetDynamicFilterCoroutineUseCase(
        private val dynamicFilterRepository: Repository<DynamicFilterModel>
): UseCase<DynamicFilterModel>() {

    override suspend fun executeOnBackground(): DynamicFilterModel {
        return dynamicFilterRepository.getResponse(useCaseRequestParams.parameters)
    }
}