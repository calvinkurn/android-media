package com.tokopedia.tokofood.feature.ordertracking.domain.mapper

import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderStatusResponse
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderStatusLiveTrackingUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ToolbarLiveTrackingUiModel
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
        invoiceOrderNumberUiModel = mapToInvoiceOrderNumberUiModel(
            tokofoodOrderDetail.invoice,
            tokofoodOrderDetail.payment.paymentDate
        ),
        toolbarLiveTrackingUiModel = mapToToolbarLiveTrackingUiModel(tokofoodOrderDetail),
    )

    private fun mapToToolbarLiveTrackingUiModel(
        tokofoodOrderDetail: TokoFoodOrderStatusResponse.TokofoodOrderDetail
    ) = ToolbarLiveTrackingUiModel(
        merchantName = "",
        orderStatusTitle = tokofoodOrderDetail.orderStatus.title,
        composeEstimation = if (tokofoodOrderDetail.eta != null) {
            StringBuilder().apply {
                append(tokofoodOrderDetail.eta.label)
                append(" ")
                append(tokofoodOrderDetail.eta.time)
            }.toString()
        } else ""
    )
}
