package com.tokopedia.search.result.domain.usecase.searchcoupon

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.search.result.domain.model.SearchRedeemCouponModel
import com.tokopedia.usecase.coroutines.UseCase

internal class SearchRedeemCouponCoroutineUseCase(
    private val graphqlUseCase: GraphqlUseCase<SearchRedeemCouponModel>
) : UseCase<SearchRedeemCouponModel>() {

    override suspend fun executeOnBackground(): SearchRedeemCouponModel {
        graphqlUseCase.setTypeClass(SearchRedeemCouponModel::class.java)
        graphqlUseCase.setGraphqlQuery(RedeemCoupon())
        graphqlUseCase.setRequestParams(useCaseRequestParams.parameters)
        return graphqlUseCase.executeOnBackground()
    }
}
