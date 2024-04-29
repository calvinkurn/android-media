package com.tokopedia.search.result.domain.usecase.searchcoupon

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.search.result.domain.model.SearchCouponModel
import com.tokopedia.usecase.coroutines.UseCase


internal class SearchGetCouponCoroutineUseCase(
    private val graphqlUseCase: GraphqlUseCase<SearchCouponModel>
) : UseCase<SearchCouponModel>() {

    override suspend fun executeOnBackground(): SearchCouponModel {
        graphqlUseCase.setTypeClass(SearchCouponModel::class.java)
        graphqlUseCase.setGraphqlQuery(GetCoupon())
        graphqlUseCase.setRequestParams(useCaseRequestParams.parameters)
        return graphqlUseCase.executeOnBackground()
    }
}
