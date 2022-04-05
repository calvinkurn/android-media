package com.tokopedia.tokofood.feature.ordertracking.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderDetailResponse
import com.tokopedia.tokofood.feature.ordertracking.domain.query.TokoFoodOrderDetailQuery
import javax.inject.Inject

class TokoFoodOrderDetailUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<TokoFoodOrderDetailResponse>
) {
    init {
        useCase.setGraphqlQuery(TokoFoodOrderDetailQuery)
        useCase.setTypeClass(TokoFoodOrderDetailResponse::class.java)
    }

    suspend fun execute(orderId: String) {
        useCase.setRequestParams(TokoFoodOrderDetailQuery.createRequestParamsOrderDetail(orderId))
        //todo mapping
        try {
            useCase.executeOnBackground()
        } catch (e: Exception) {

        }
    }
}