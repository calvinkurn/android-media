package com.tokopedia.search.result.domain.usecase.getdynamicfilter

import com.tokopedia.discovery.common.coroutines.Repository
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.result.domain.usecase.SearchUseCase

class GetDynamicFilterCoroutineUseCase(
        private val repository: Repository<DynamicFilterModel>
): SearchUseCase<DynamicFilterModel>() {

    override suspend fun executeOnBackground(): DynamicFilterModel {
        return repository.query(requestParams.toMap())
    }
}
