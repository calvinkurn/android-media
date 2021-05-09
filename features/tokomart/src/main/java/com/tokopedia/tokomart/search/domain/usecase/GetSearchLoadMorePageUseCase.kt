package com.tokopedia.tokomart.search.domain.usecase

import com.tokopedia.tokomart.search.domain.model.SearchModel
import com.tokopedia.usecase.coroutines.UseCase

class GetSearchLoadMorePageUseCase: UseCase<SearchModel>() {

    override suspend fun executeOnBackground(): SearchModel {
        return SearchModel()
    }
}