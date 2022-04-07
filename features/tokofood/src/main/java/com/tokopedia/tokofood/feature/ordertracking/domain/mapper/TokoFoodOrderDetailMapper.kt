package com.tokopedia.tokofood.feature.ordertracking.domain.mapper

import com.tokopedia.tokofood.feature.ordertracking.domain.constants.OrderStatusType
import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderDetailResponse
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailResultUiModel

import javax.inject.Inject

class TokoFoodOrderDetailMapper @Inject constructor(
    private val orderLiveTrackingMapper: TokoFoodOrderLiveTrackingMapper,
    private val orderCompletedMapper: TokoFoodOrderCompletedMapper
) {

    fun mapToOrderDetailResultUiModel(orderDetailResponse: TokoFoodOrderDetailResponse.TokofoodOrderDetail): OrderDetailResultUiModel {

        val orderStatus = orderDetailResponse.orderStatus.status

        val isOrderCompleted =
            orderStatus in listOf(OrderStatusType.CANCELLED, OrderStatusType.COMPLETED)

        val orderDetailList = when {
            isOrderCompleted -> {
                orderCompletedMapper.mapToOrderDetailList(orderDetailResponse)
            }
            else -> {
                orderLiveTrackingMapper.mapToOrderDetailList(orderDetailResponse)
            }
        }

        return OrderDetailResultUiModel(
            orderDetailList,
            isOrderCompleted
        )
    }
}