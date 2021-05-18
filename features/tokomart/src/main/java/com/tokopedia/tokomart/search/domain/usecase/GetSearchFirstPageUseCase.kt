package com.tokopedia.tokomart.search.domain.usecase

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_SOURCE_QUICK_FILTER
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.discovery.common.utils.UrlParamUtils.generateUrlParamString
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.tokomart.search.domain.model.SearchModel
import com.tokopedia.tokomart.searchcategory.data.mapper.getSearchProduct
import com.tokopedia.tokomart.searchcategory.domain.model.FilterModel
import com.tokopedia.tokomart.searchcategory.utils.QUICK_FILTER_TOKONOW
import com.tokopedia.usecase.coroutines.UseCase

class GetSearchFirstPageUseCase(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
): UseCase<SearchModel>() {

    override suspend fun executeOnBackground(): SearchModel {
        val params = useCaseRequestParams.parameters
        val quickFilterParams = createQuickFilterParams(params)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(createAceSearchProductRequest(generateUrlParamString(params)))
        graphqlUseCase.addRequest(createQuickFilterRequest(generateUrlParamString(quickFilterParams)))

        val graphqlResponse = graphqlUseCase.executeOnBackground()

        return SearchModel(
                searchProduct = getSearchProduct(graphqlResponse),
                quickFilter = getQuickFilter(graphqlResponse),
        )
    }

    private fun createQuickFilterParams(params: Map<String, Any>): Map<String?, Any> {
        return params.toMutableMap<String?, Any>().also {
//            it[SearchApiConst.SOURCE] = QUICK_FILTER_TOKONOW // Temporary, source should be quick filter tokonow
            it[SearchApiConst.SOURCE] = DEFAULT_VALUE_SOURCE_QUICK_FILTER
        }
    }

    private fun getQuickFilter(graphqlResponse: GraphqlResponse): DataValue {
        return graphqlResponse.getData<FilterModel?>(FilterModel::class.java)
                ?.filterSortProduct
                ?.data ?: DataValue()
    }
}