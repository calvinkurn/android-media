package com.tokopedia.tokopedianow.searchcategory.domain.usecase

import com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.helper.FilterSortProductRequest.FILTER_SORT_PRODUCT_GQL
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tokopedianow.searchcategory.domain.model.FilterModel
import com.tokopedia.usecase.coroutines.UseCase

class GetFilterUseCase(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
): UseCase<DynamicFilterModel>() {

    override suspend fun executeOnBackground(): DynamicFilterModel {
        val params = useCaseRequestParams.parameters

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(
                GraphqlRequest(
                        FILTER_SORT_PRODUCT_GQL,
                        FilterModel::class.java,
                        mapOf(KEY_PARAMS to UrlParamUtils.generateUrlParamString(params))
                )
        )

        val graphqlResponse = graphqlUseCase.executeOnBackground()

        return graphqlResponse.getData<FilterModel?>(FilterModel::class.java)
                ?.filterSortProduct ?: DynamicFilterModel()
    }
}
