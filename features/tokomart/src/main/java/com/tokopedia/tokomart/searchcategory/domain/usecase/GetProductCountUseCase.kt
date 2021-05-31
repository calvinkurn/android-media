package com.tokopedia.tokomart.searchcategory.domain.usecase

import com.tokopedia.usecase.coroutines.UseCase

class GetProductCountUseCase: UseCase<String>() {

    override suspend fun executeOnBackground(): String {
        return ""
    }
}