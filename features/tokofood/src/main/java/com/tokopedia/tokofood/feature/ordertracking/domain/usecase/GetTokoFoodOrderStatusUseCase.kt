package com.tokopedia.tokofood.feature.ordertracking.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.TokoFoodOrderStatusMapper
import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderStatusResponse
import com.tokopedia.tokofood.feature.ordertracking.domain.query.TokoFoodOrderStatusQuery
import javax.inject.Inject

class GetTokoFoodOrderStatusUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<TokoFoodOrderStatusResponse>,
    private val mapper: TokoFoodOrderStatusMapper
) {
    init {
        useCase.setGraphqlQuery(TokoFoodOrderStatusQuery)
        useCase.setTypeClass(TokoFoodOrderStatusResponse::class.java)
    }

    suspend fun execute(orderId: String) {
        useCase.setRequestParams(TokoFoodOrderStatusQuery.createRequestParamsOrderDetail(orderId))
        try {
            mapper.mapToOrderStatusLiveTrackingUiModel(useCase.executeOnBackground().tokofoodOrderDetail)
        } catch (e: Exception) {
            throw MessageErrorException(ORDER_STATUS_POOL_STATE, e.localizedMessage)
        }
    }

    companion object {
        const val ORDER_STATUS_POOL_STATE = "orderStatusPoolState"
    }
}