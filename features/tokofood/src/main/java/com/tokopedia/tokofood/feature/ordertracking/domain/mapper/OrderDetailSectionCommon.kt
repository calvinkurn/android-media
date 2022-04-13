package com.tokopedia.tokofood.feature.ordertracking.domain.mapper

import com.tokopedia.iconunify.IconUnify
import com.tokopedia.tokofood.common.LottieUrl
import com.tokopedia.tokofood.feature.ordertracking.domain.constants.OrderStatusType
import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderDetailResponse
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.InvoiceOrderNumberUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingEstimationUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingStatusInfoUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.StepperStatusUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.TickerInfoData
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData

abstract class OrderDetailSectionCommon {

    fun mapToTickerInfoData(
        additionalTickerInfo:
        List<TokoFoodOrderDetailResponse.TokofoodOrderDetail.AdditionalTickerInfo>
    ) = TickerInfoData(additionalTickerInfo.map {
        TickerData(
            title = "",
            description = it.appText,
            type = Ticker.TYPE_INFORMATION,
            isFromHtml = true
        )
    })

    fun mapToEstimationUiModel(
        eta: TokoFoodOrderDetailResponse.TokofoodOrderDetail.Eta
    ) = OrderTrackingEstimationUiModel(
        estimationLabel = eta.label,
        estimationTime = eta.time
    )

    fun mapToInvoiceOrderNumberUiModel(
        invoice: TokoFoodOrderDetailResponse.TokofoodOrderDetail.Invoice,
        paymentDate: String
    ) = InvoiceOrderNumberUiModel(
        invoiceNumber = invoice.invoiceNumber,
        goFoodOrderNumber = invoice.gofoodOrderNumber.orEmpty(),
        paymentDate = paymentDate
    )

    protected fun getOrderStatusLottieUrl(orderStatus: String): String {
        return when (orderStatus) {
            OrderStatusType.NEW, OrderStatusType.CREATED -> {
                LottieUrl.OrderTracking.HOURGLASS
            }
            OrderStatusType.AWAITING_MERCHANT_ACCEPTANCE,
            OrderStatusType.MERCHANT_ACCEPTED, OrderStatusType.SEARCHING_DRIVER -> {
                LottieUrl.OrderTracking.CHECK
            }
            OrderStatusType.OTW_PICKUP, OrderStatusType.DRIVER_ARRIVED,
            OrderStatusType.PICKUP_REQUESTED, OrderStatusType.ORDER_PLACED -> {
                LottieUrl.OrderTracking.COOK
            }
            OrderStatusType.OTW_DESTINATION, OrderStatusType.FRAUD_CHECK -> {
                LottieUrl.OrderTracking.DELIVER
            }
            else -> ""
        }
    }

    protected fun getHourGlassStepperStatus(
        isIconActive: Boolean = true,
        isLineActive: Boolean
    ): StepperStatusUiModel {
        return StepperStatusUiModel(
            isIconActive = isIconActive,
            isLineActive = isLineActive,
            iconName = IconUnify.HOURGLASS
        )
    }

    protected fun getCookStepperStatus(
        isIconActive: Boolean,
        isLineActive: Boolean
    ): StepperStatusUiModel {
        return StepperStatusUiModel(
            isIconActive = isIconActive,
            isLineActive = isLineActive,
            iconName = IconUnify.COOK
        )
    }

    protected fun getDriverStepperStatus(
        isIconActive: Boolean,
        isLineActive: Boolean
    ): StepperStatusUiModel {
        return StepperStatusUiModel(
            isIconActive = isIconActive,
            isLineActive = isLineActive,
            iconName = IconUnify.DRIVER
        )
    }

    protected fun getProductMoveStepperStatus(
        isIconActive: Boolean = false,
        isLineActive: Boolean = false
    ): StepperStatusUiModel {
        return StepperStatusUiModel(
            isIconActive = isIconActive,
            isLineActive = isLineActive,
            iconName = IconUnify.PRODUCT_MOVE
        )
    }

    protected fun mapToOrderTrackingStatusInfo(
        orderStatus: TokoFoodOrderDetailResponse.TokofoodOrderDetail.OrderStatus
    ) = OrderTrackingStatusInfoUiModel(
        stepperStatusList = mapToStepperStatusList(orderStatus.status),
        statusKey = orderStatus.status,
        orderStatusTitle = orderStatus.title,
        orderStatusSubTitle = orderStatus.subtitle,
        lottieUrl = getOrderStatusLottieUrl(orderStatus.status)
    )

    private fun mapToStepperStatusList(orderStatus: String): List<StepperStatusUiModel> {
        return when (orderStatus) {
            OrderStatusType.NEW, OrderStatusType.CREATED -> {
                listOf(
                    getHourGlassStepperStatus(isLineActive = false),
                    getCookStepperStatus(isIconActive = false, isLineActive = false),
                    getDriverStepperStatus(isIconActive = false, isLineActive = false),
                    getProductMoveStepperStatus()
                )
            }
            OrderStatusType.AWAITING_MERCHANT_ACCEPTANCE,
            OrderStatusType.MERCHANT_ACCEPTED, OrderStatusType.SEARCHING_DRIVER -> {
                listOf(
                    getHourGlassStepperStatus(isLineActive = true),
                    getCookStepperStatus(isIconActive = false, isLineActive = false),
                    getDriverStepperStatus(isIconActive = false, isLineActive = false),
                    getProductMoveStepperStatus()
                )
            }
            OrderStatusType.OTW_PICKUP, OrderStatusType.DRIVER_ARRIVED,
            OrderStatusType.PICKUP_REQUESTED, OrderStatusType.ORDER_PLACED -> {
                listOf(
                    getHourGlassStepperStatus(isLineActive = true),
                    getCookStepperStatus(isIconActive = true, isLineActive = true),
                    getDriverStepperStatus(isIconActive = false, isLineActive = false),
                    getProductMoveStepperStatus()
                )
            }
            OrderStatusType.OTW_DESTINATION, OrderStatusType.FRAUD_CHECK -> {
                listOf(
                    getHourGlassStepperStatus(isLineActive = true),
                    getCookStepperStatus(isIconActive = true, isLineActive = true),
                    getDriverStepperStatus(isIconActive = true, isLineActive = true),
                    getProductMoveStepperStatus()
                )
            }
            else -> listOf(
                getHourGlassStepperStatus(isLineActive = false),
                getCookStepperStatus(isIconActive = false, isLineActive = false),
                getDriverStepperStatus(isIconActive = false, isLineActive = false),
                getProductMoveStepperStatus()
            )
        }
    }
}