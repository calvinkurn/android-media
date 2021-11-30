package com.tokopedia.tokopedianow.search.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.searchcategory.data.createAceSearchProductRequest
import com.tokopedia.tokopedianow.searchcategory.data.getTokonowQueryParam
import com.tokopedia.tokopedianow.searchcategory.data.mapper.getSearchProduct
import com.tokopedia.usecase.coroutines.UseCase

class GetSearchLoadMorePageUseCase(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
): UseCase<SearchModel>() {

    override suspend fun executeOnBackground(): SearchModel {
        val queryParams = getTokonowQueryParam(useCaseRequestParams)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(createAceSearchProductRequest(queryParams))

        val graphqlResponse = graphqlUseCase.executeOnBackground()

        return SearchModel(
                searchProduct = getSearchProduct(graphqlResponse)
        )
    }
}