package com.tokopedia.tokofood.feature.ordertracking.domain.mapper

import com.tokopedia.iconunify.IconUnify
import com.tokopedia.tokofood.common.constants.LottieUrl
import com.tokopedia.tokofood.feature.ordertracking.domain.constants.OrderStatusType
import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderDetailResponse
import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderStatusResponse
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DriverInformationUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DriverSectionUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.InvoiceOrderNumberUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingEstimationUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingStatusInfoUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.StepperStatusUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.TickerInfoData
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ToolbarLiveTrackingUiModel
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
    ): InvoiceOrderNumberUiModel {
        return InvoiceOrderNumberUiModel(
            invoiceNumber = invoice.invoiceNumber,
            invoiceUrl = invoice.invoiceURL,
            goFoodOrderNumber = invoice.gofoodOrderNumber.orEmpty(),
            paymentDate = paymentDate
        )
    }

    fun mapToDriverSectionUiModel(
        driverDetails: TokoFoodOrderDetailResponse.TokofoodOrderDetail.DriverDetails?,
        orderStatus: TokoFoodOrderDetailResponse.TokofoodOrderDetail.OrderStatus,
        invoice: TokoFoodOrderDetailResponse.TokofoodOrderDetail.Invoice
    ): DriverSectionUiModel? {
        return if (driverDetails != null) {
            val isCallable = orderStatus.status !in listOf(OrderStatusType.COMPLETED, OrderStatusType.CANCELLED)
            return DriverSectionUiModel(
                driverInformationList = mapToDriverInformationList(driverDetails.karma),
                name = driverDetails.name,
                photoUrl = driverDetails.photoUrl,
                licensePlateNumber = driverDetails.licensePlateNumber,
                isCallable = isCallable,
                isEnableChat = orderStatus.isEnableChatButton,
                goFoodOrderNumber = invoice.gofoodOrderNumber.orEmpty(),
            )
        } else {
            null
        }
    }

    private fun mapToDriverInformationList(
        karma: List<TokoFoodOrderDetailResponse.TokofoodOrderDetail.DriverDetails.Karma>?
    ): List<DriverInformationUiModel> {
        return karma?.map {
            DriverInformationUiModel(iconInformationUrl = it.icon, informationName = it.message)
        }.orEmpty()
    }

    private fun getOrderStatusLottieUrl(orderStatus: String): String {
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

    private fun getHourGlassStepperStatus(
        isIconActive: Boolean = true,
        isLineActive: Boolean
    ): StepperStatusUiModel {
        return StepperStatusUiModel(
            isIconActive = isIconActive,
            isLineActive = isLineActive,
            iconName = IconUnify.HOURGLASS
        )
    }

    private fun getCookStepperStatus(
        isIconActive: Boolean,
        isLineActive: Boolean
    ): StepperStatusUiModel {
        return StepperStatusUiModel(
            isIconActive = isIconActive,
            isLineActive = isLineActive,
            iconName = IconUnify.COOK
        )
    }

    private fun getDriverStepperStatus(
        isIconActive: Boolean,
        isLineActive: Boolean
    ): StepperStatusUiModel {
        return StepperStatusUiModel(
            isIconActive = isIconActive,
            isLineActive = isLineActive,
            iconName = IconUnify.DRIVER
        )
    }

    private fun getProductMoveStepperStatus(
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
        orderStatusSubTitle = orderStatus.subtitle.orEmpty(),
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
