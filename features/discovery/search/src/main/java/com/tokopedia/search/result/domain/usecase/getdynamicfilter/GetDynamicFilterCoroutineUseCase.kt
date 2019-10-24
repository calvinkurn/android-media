package com.tokopedia.search.result.domain.usecase.getdynamicfilter

import com.tokopedia.discovery.common.coroutines.Repository
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.result.common.UseCase

internal class GetDynamicFilterCoroutineUseCase(
        private val dynamicFilterRepository: Repository<DynamicFilterModel>
): UseCase<DynamicFilterModel>() {

    override suspend fun execute(inputParameter: Map<String, Any>): DynamicFilterModel {
        return dynamicFilterRepository.getResponse(inputParameter)
    }
}