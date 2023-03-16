package com.tokopedia.search.result.mps.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.usecase.coroutines.UseCase

class MPSLoadMoreUseCase(
    private val graphqlUseCase: MultiRequestGraphqlUseCase,
): UseCase<MPSModel>() {

    override suspend fun executeOnBackground(): MPSModel {
        return MPSModel()
    }
}
