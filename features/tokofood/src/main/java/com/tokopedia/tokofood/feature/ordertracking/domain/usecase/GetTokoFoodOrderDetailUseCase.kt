package com.tokopedia.tokofood.feature.ordertracking.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.TokoFoodOrderDetailMapper
import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderDetailResponse
import com.tokopedia.tokofood.feature.ordertracking.domain.query.TOKO_FOOD_ORDER_DETAIL_QUERY
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailResultUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("TokoFoodOrderDetailQuery", TOKO_FOOD_ORDER_DETAIL_QUERY)
open class GetTokoFoodOrderDetailUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<TokoFoodOrderDetailResponse>,
    private val tokoFoodOrderDetailMapper: TokoFoodOrderDetailMapper
) {
    init {
        useCase.setGraphqlQuery(TokoFoodOrderDetailQuery())
        useCase.setTypeClass(TokoFoodOrderDetailResponse::class.java)
    }

    open suspend fun execute(orderId: String): OrderDetailResultUiModel {
        useCase.setRequestParams(createRequestParamsOrderDetail(orderId))
        val response = useCase.executeOnBackground().tokofoodOrderDetail
        return tokoFoodOrderDetailMapper.mapToOrderDetailResultUiModel(response)
    }

    fun createRequestParamsOrderDetail(orderId: String): Map<String, Any> {
        return RequestParams.create().apply {
            putString(ORDER_ID_KEY, orderId)
        }.parameters
    }

    companion object {
        const val ORDER_ID_KEY = "orderID"
    }
}
