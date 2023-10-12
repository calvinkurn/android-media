package com.tokopedia.tokofood.stub.postpurchase.domain.usecase

import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.TokoFoodOrderDetailMapper
import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderDetailResponse
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.GetTokoFoodOrderDetailUseCase
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailResultUiModel
import com.tokopedia.tokofood.stub.common.graphql.interactor.GraphqlUseCaseStub

class GetTokoFoodOrderDetailUseCaseStub(
    private val useCase: GraphqlUseCaseStub<TokoFoodOrderDetailResponse>,
    private val tokoFoodOrderDetailMapper: TokoFoodOrderDetailMapper
) : GetTokoFoodOrderDetailUseCase(useCase, tokoFoodOrderDetailMapper) {

    var responseStub = TokoFoodOrderDetailResponse()
        set(value) {
            useCase.createMapResult(TokoFoodOrderDetailResponse::class.java, value)
            field = value
        }

    override suspend fun execute(orderId: String): OrderDetailResultUiModel {
        useCase.setRequestParams(createRequestParamsOrderDetail(orderId))
        val response = useCase.executeOnBackground().tokofoodOrderDetail
        return tokoFoodOrderDetailMapper.mapToOrderDetailResultUiModel(response)
    }
}
