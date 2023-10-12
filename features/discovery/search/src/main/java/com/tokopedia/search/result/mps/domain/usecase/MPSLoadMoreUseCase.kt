package com.tokopedia.search.result.mps.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.usecase.coroutines.UseCase

class MPSLoadMoreUseCase(
    private val graphqlUseCase: MultiRequestGraphqlUseCase,
): UseCase<MPSModel>() {

    override suspend fun executeOnBackground(): MPSModel = graphqlUseCase.run {
        val requestParams = useCaseRequestParams.parameters as Map<String?, String>

        clearRequest()
        addRequest(mpsRequest(requestParams))

        MPSModel(searchShopMPS = executeOnBackground().getSearchShopMPS())
    }
}
