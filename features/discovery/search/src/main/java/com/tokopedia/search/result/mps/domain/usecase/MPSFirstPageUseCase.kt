package com.tokopedia.search.result.mps.domain.usecase

import com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PAGE_SOURCE
import com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS
import com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_SOURCE
import com.tokopedia.discovery.common.constants.SearchConstant.GQL.PAGE_SOURCE_MPS
import com.tokopedia.discovery.common.constants.SearchConstant.GQL.SOURCE_QUICK_FILTER
import com.tokopedia.filter.common.helper.FilterSortProduct
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.search.result.mps.domain.model.MPSQuickFilterModel
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.usecase.coroutines.UseCase

class MPSFirstPageUseCase(
    private val graphqlUseCase: MultiRequestGraphqlUseCase,
): UseCase<MPSModel>() {

    override suspend fun executeOnBackground(): MPSModel {
        val requestParams = useCaseRequestParams.parameters as Map<String?, String>

        return graphqlUseCase.run {
            clearRequest()
            addRequests(listOf(
                mpsRequest(requestParams),
                quickFilterRequest()
            ))

            val graphqlResponse = executeOnBackground()

            MPSModel(
                searchShopMPS = graphqlResponse.getSearchShopMPS(),
                quickFilterModel = graphqlResponse.getQuickFilter().quickFilterModel,
            )
        }
    }

    private fun quickFilterRequest(): GraphqlRequest {
        val quickFilterParams = useCaseRequestParams.parameters.toMutableMap()
        quickFilterParams[KEY_PAGE_SOURCE] = PAGE_SOURCE_MPS
        quickFilterParams[KEY_SOURCE] = SOURCE_QUICK_FILTER

        return GraphqlRequest(
            FilterSortProduct(),
            MPSQuickFilterModel::class.java,
            mapOf(
                KEY_PARAMS to UrlParamUtils.generateUrlParamString(quickFilterParams)
            )
        )
    }

    private fun GraphqlResponse.getQuickFilter(): MPSQuickFilterModel =
        getData<MPSQuickFilterModel>(MPSQuickFilterModel::class.java) ?: MPSQuickFilterModel()
}
