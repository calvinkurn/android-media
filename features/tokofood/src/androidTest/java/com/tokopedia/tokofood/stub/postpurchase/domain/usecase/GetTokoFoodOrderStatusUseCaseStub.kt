package com.tokopedia.tokofood.stub.postpurchase.domain.usecase

import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.TokoFoodOrderStatusMapper
import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderStatusResponse
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.GetTokoFoodOrderStatusUseCase
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderStatusLiveTrackingUiModel
import com.tokopedia.tokofood.stub.common.graphql.interactor.GraphqlUseCaseStub

class GetTokoFoodOrderStatusUseCaseStub(
    private val useCase: GraphqlUseCaseStub<TokoFoodOrderStatusResponse>,
    private val tokoFoodOrderStatusMapper: TokoFoodOrderStatusMapper
) : GetTokoFoodOrderStatusUseCase(
    useCase,
    tokoFoodOrderStatusMapper
) {

    var responseStub = TokoFoodOrderStatusResponse()
        set(value) {
            useCase.createMapResult(TokoFoodOrderStatusResponse::class.java, value)
            field = value
        }

    override suspend fun execute(orderId: String): OrderStatusLiveTrackingUiModel {
        useCase.setRequestParams(createRequestParamsOrderDetail(orderId))
        val response = useCase.executeOnBackground().tokofoodOrderDetail
        return tokoFoodOrderStatusMapper.mapToOrderStatusLiveTrackingUiModel(response)
    }
}
