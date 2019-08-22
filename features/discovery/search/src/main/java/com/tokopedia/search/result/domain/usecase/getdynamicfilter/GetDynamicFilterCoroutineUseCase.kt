package com.tokopedia.search.result.domain.usecase.getdynamicfilter

import com.tokopedia.discovery.common.data.DynamicFilterModel
import com.tokopedia.search.result.domain.usecase.SearchUseCase
import kotlinx.coroutines.delay

class GetDynamicFilterCoroutineUseCase: SearchUseCase<DynamicFilterModel>() {

    override suspend fun executeOnBackground(): DynamicFilterModel {
        delay(1500)
        return DynamicFilterModel()
    }
}
