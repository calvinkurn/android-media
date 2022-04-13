package com.tokopedia.tokofood.feature.ordertracking.domain.mapper

import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderStatusResponse
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderStatusLiveTrackingUiModel
import javax.inject.Inject

class TokoFoodOrderStatusMapper @Inject constructor() : OrderDetailSectionCommon() {

    fun mapToOrderStatusLiveTrackingUiModel(
        tokofoodOrderDetail: TokoFoodOrderStatusResponse.TokofoodOrderDetail
    ) = OrderStatusLiveTrackingUiModel(
        tickerInfoData = if (tokofoodOrderDetail.additionalTickerInfo != null) mapToTickerInfoData(
            tokofoodOrderDetail.additionalTickerInfo
        ) else null,
        orderTrackingStatusInfoUiModel = mapToOrderTrackingStatusInfo(tokofoodOrderDetail.orderStatus),
        orderStatusKey = tokofoodOrderDetail.orderStatus.status,
        estimationUiModel = if (tokofoodOrderDetail.eta != null)
            mapToEstimationUiModel(tokofoodOrderDetail.eta) else null,
    )
}