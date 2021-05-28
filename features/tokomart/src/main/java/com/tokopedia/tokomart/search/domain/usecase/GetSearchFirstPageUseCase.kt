package com.tokopedia.tokomart.search.domain.usecase

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.tokomart.search.domain.model.SearchModel
import com.tokopedia.tokomart.searchcategory.data.createAceSearchProductRequest
import com.tokopedia.tokomart.searchcategory.data.createCategoryFilterRequest
import com.tokopedia.tokomart.searchcategory.data.createQuickFilterRequest
import com.tokopedia.tokomart.searchcategory.data.getTokonowQueryParam
import com.tokopedia.tokomart.searchcategory.data.mapper.getCategoryFilter
import com.tokopedia.tokomart.searchcategory.data.mapper.getQuickFilter
import com.tokopedia.tokomart.searchcategory.data.mapper.getSearchProduct
import com.tokopedia.tokomart.searchcategory.utils.CATEGORY_TOKONOW
import com.tokopedia.tokomart.searchcategory.utils.QUICK_FILTER_TOKONOW
import com.tokopedia.usecase.coroutines.UseCase

class GetSearchFirstPageUseCase(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
): UseCase<SearchModel>() {

    override suspend fun executeOnBackground(): SearchModel {
        val queryParams = getTokonowQueryParam(useCaseRequestParams)
        val categoryFilterParams = createCategoryFilterParams(queryParams)
        val quickFilterParams = createQuickFilterParams(queryParams)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(createAceSearchProductRequest(queryParams))
        graphqlUseCase.addRequest(createCategoryFilterRequest(categoryFilterParams))
        graphqlUseCase.addRequest(createQuickFilterRequest(quickFilterParams))

        val graphqlResponse = graphqlUseCase.executeOnBackground()

        return SearchModel(
                searchProduct = getSearchProduct(graphqlResponse),
                categoryFilter = getCategoryFilter(graphqlResponse),
                quickFilter = getQuickFilter(graphqlResponse),
        )
    }

    private fun createCategoryFilterParams(queryParams: Map<String?, Any>): Map<String?, Any> {
        return queryParams.toMutableMap().also {
            it[SearchApiConst.SOURCE] = CATEGORY_TOKONOW
        }
    }

    private fun createQuickFilterParams(queryParams: Map<String?, Any>): Map<String?, Any> {
        return queryParams.toMutableMap().also {
            it[SearchApiConst.SOURCE] = QUICK_FILTER_TOKONOW
        }
    }
}