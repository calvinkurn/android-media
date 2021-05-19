package com.tokopedia.tokomart.search.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.tokomart.search.domain.model.SearchModel
import com.tokopedia.tokomart.searchcategory.data.createAceSearchProductRequest
import com.tokopedia.tokomart.searchcategory.data.createCategoryFilterRequest
import com.tokopedia.tokomart.searchcategory.data.createQuickFilterRequest
import com.tokopedia.tokomart.searchcategory.data.mapper.getCategoryFilter
import com.tokopedia.tokomart.searchcategory.data.mapper.getQuickFilter
import com.tokopedia.tokomart.searchcategory.data.mapper.getSearchProduct
import com.tokopedia.usecase.coroutines.UseCase

class GetSearchFirstPageUseCase(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
): UseCase<SearchModel>() {

    override suspend fun executeOnBackground(): SearchModel {
        val params = useCaseRequestParams.parameters

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(createAceSearchProductRequest(params))
        graphqlUseCase.addRequest(createCategoryFilterRequest(params))
        graphqlUseCase.addRequest(createQuickFilterRequest(params))

        val graphqlResponse = graphqlUseCase.executeOnBackground()

        return SearchModel(
                searchProduct = getSearchProduct(graphqlResponse),
                categoryFilter = getCategoryFilter(graphqlResponse),
                quickFilter = getQuickFilter(graphqlResponse),
        )
    }
}