package com.tokopedia.tokomart.search.domain.usecase

import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.tokomart.search.domain.model.SearchModel
import com.tokopedia.tokomart.searchcategory.data.mapper.getSearchProduct
import com.tokopedia.usecase.coroutines.UseCase

class GetSearchFirstPageUseCase(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
): UseCase<SearchModel>() {

    override suspend fun executeOnBackground(): SearchModel {
        val params = useCaseRequestParams.parameters

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(createAceSearchProductGraphqlRequest(UrlParamUtils.generateUrlParamString(params)))

        val graphqlResponse = graphqlUseCase.executeOnBackground()

        return SearchModel(
                searchProduct = getSearchProduct(graphqlResponse)
        )
    }
}