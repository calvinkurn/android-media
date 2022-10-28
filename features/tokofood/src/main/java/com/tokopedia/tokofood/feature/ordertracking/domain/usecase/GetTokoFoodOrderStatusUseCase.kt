package com.tokopedia.tokofood.feature.ordertracking.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.TokoFoodOrderStatusMapper
import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderStatusResponse
import com.tokopedia.tokofood.feature.ordertracking.domain.query.TOKO_FOOD_ORDER_STATUS_QUERY
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderStatusLiveTrackingUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("TokoFoodOrderStatusQuery", TOKO_FOOD_ORDER_STATUS_QUERY)
class GetTokoFoodOrderStatusUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<TokoFoodOrderStatusResponse>,
    private val mapper: TokoFoodOrderStatusMapper
) {
    init {
        useCase.setGraphqlQuery(TokoFoodOrderStatusQuery())
        useCase.setTypeClass(TokoFoodOrderStatusResponse::class.java)
    }

    suspend fun execute(orderId: String): OrderStatusLiveTrackingUiModel {
        useCase.setRequestParams(createRequestParamsOrderDetail(orderId))
        return try {
            mapper.mapToOrderStatusLiveTrackingUiModel(useCase.executeOnBackground().tokofoodOrderDetail)
        } catch (e: Exception) {
            throw MessageErrorException("${ORDER_STATUS_POOL_STATE}: ${e.localizedMessage}")
        }
    }

    private fun createRequestParamsOrderDetail(orderId: String): Map<String, Any> {
        return RequestParams.create().apply {
            putString(ORDER_ID_KEY, orderId)
        }.parameters
    }

    companion object {
        private const val ORDER_ID_KEY = "orderID"
        private const val ORDER_STATUS_POOL_STATE = "orderStatusPoolState"
    }
}