package com.tokopedia.tokofood.feature.ordertracking.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderStatusResponse
import com.tokopedia.tokofood.feature.ordertracking.domain.query.TokoFoodOrderStatusQuery
import javax.inject.Inject

class GetTokoFoodOrderStatusUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<TokoFoodOrderStatusResponse>
) {
    init {
        useCase.setGraphqlQuery(TokoFoodOrderStatusQuery)
        useCase.setTypeClass(TokoFoodOrderStatusResponse::class.java)
    }

    suspend fun execute(orderId: String) {
        useCase.setRequestParams(TokoFoodOrderStatusQuery.createRequestParamsOrderDetail(orderId))
        try {
            useCase.executeOnBackground()
        } catch (e: Exception) {

        }
    }
}