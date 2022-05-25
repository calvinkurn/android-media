package com.tokopedia.tokofood.feature.ordertracking.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.TokoFoodOrderDetailMapper
import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderDetailResponse
import com.tokopedia.tokofood.feature.ordertracking.domain.query.TokoFoodOrderDetailQuery
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailResultUiModel
import javax.inject.Inject

class GetTokoFoodOrderDetailUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<TokoFoodOrderDetailResponse>,
    private val tokoFoodOrderDetailMapper: TokoFoodOrderDetailMapper
) {
    init {
        useCase.setGraphqlQuery(TokoFoodOrderDetailQuery)
        useCase.setTypeClass(TokoFoodOrderDetailResponse::class.java)
    }

    suspend fun execute(orderId: String): OrderDetailResultUiModel {
        useCase.setRequestParams(TokoFoodOrderDetailQuery.createRequestParamsOrderDetail(orderId))
        val response = useCase.executeOnBackground().tokofoodOrderDetail
        return tokoFoodOrderDetailMapper.mapToOrderDetailResultUiModel(response)
    }
}