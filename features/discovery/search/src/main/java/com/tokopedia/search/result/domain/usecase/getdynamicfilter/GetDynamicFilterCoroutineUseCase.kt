package com.tokopedia.search.result.domain.usecase.getdynamicfilter

import com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.helper.FilterSortProduct
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.search.result.data.response.GqlDynamicFilterResponse
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.usecase.coroutines.UseCase

class GetDynamicFilterCoroutineUseCase (
        private val graphqlUseCase: GraphqlUseCase<GqlDynamicFilterResponse>
): UseCase<DynamicFilterModel>() {

    override suspend fun executeOnBackground(): DynamicFilterModel {
        graphqlUseCase.setTypeClass(GqlDynamicFilterResponse::class.java)
        graphqlUseCase.setGraphqlQuery(FilterSortProduct())
        graphqlUseCase.setRequestParams(createRequestParams())

        return graphqlUseCase.executeOnBackground().dynamicFilterModel
    }

    private fun createRequestParams(): Map<String, Any> =
        mapOf(
            KEY_PARAMS to UrlParamUtils.generateUrlParamString(useCaseRequestParams.parameters)
        )
}