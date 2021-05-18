package com.tokopedia.tokomart.searchcategory.domain.usecase

import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.usecase.coroutines.UseCase

class GetFilterUseCase: UseCase<DynamicFilterModel>() {

    override suspend fun executeOnBackground(): DynamicFilterModel {
        TODO("Not yet implemented")
    }
}