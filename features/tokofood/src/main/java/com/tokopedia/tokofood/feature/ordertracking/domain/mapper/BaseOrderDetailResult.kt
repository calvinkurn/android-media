package com.tokopedia.tokofood.feature.ordertracking.domain.mapper

import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.tokofood.common.LottieUrl
import com.tokopedia.tokofood.feature.ordertracking.domain.constants.DriverInfoIcon
import com.tokopedia.tokofood.feature.ordertracking.domain.constants.OrderStatusType
import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderDetailResponse
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.BaseOrderTrackingTypeFactory
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.AddonVariantItemUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DriverInformationUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DriverSectionUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.FoodItemUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.InvoiceOrderNumberUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailHeaderUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailToggleCtaUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingEstimationUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingStatusInfoUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.PaymentAmountUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.PaymentDetailUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.PaymentHeaderUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.PaymentMethodUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ShippingHeaderUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.StepperStatusUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ThickDividerUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ThinDividerMarginUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ThinDividerUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.TickerInfoData
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData

abstract class BaseOrderDetailResult {

    abstract fun mapToOrderDetailList(tokoFoodOrderDetail: TokoFoodOrderDetailResponse.TokofoodOrderDetail): List<BaseOrderTrackingTypeFactory>

    protected fun MutableList<BaseOrderTrackingTypeFactory>.addTickerUiModel(
        additionalTickerInfo:
        List<TokoFoodOrderDetailResponse.TokofoodOrderDetail.AdditionalTickerInfo>?
    ) {
        add(TickerInfoData(additionalTickerInfo?.map {
            TickerData(
                title = "",
                description = it.notes,
                type = Ticker.TYPE_INFORMATION,
                isFromHtml = true
            )
        }.orEmpty()))
    }

    protected fun MutableList<BaseOrderTrackingTypeFactory>.addEstimationUiModel(
        eta: TokoFoodOrderDetailResponse.TokofoodOrderDetail.Eta
    ) {
        if (eta.time.isNotEmpty()) {
            add(
                OrderTrackingEstimationUiModel(
                    estimationLabel = eta.label,
                    estimationTime = eta.time
                )
            )
        }
    }

    protected fun MutableList<BaseOrderTrackingTypeFactory>.addInvoiceOrderNumberUiModel(
        invoice: TokoFoodOrderDetailResponse.TokofoodOrderDetail.Invoice,
        paymentDate: String
    ) {
        add(
            InvoiceOrderNumberUiModel(
                invoiceNumber = invoice.invoiceNumber,
                goFoodOrderNumber = invoice.gofoodOrderNumber,
                paymentDate = paymentDate
            )
        )
    }

    protected fun MutableList<BaseOrderTrackingTypeFactory>.addPaymentMethodUiModel(
        paymentMethod: TokoFoodOrderDetailResponse.TokofoodOrderDetail.Payment.PaymentMethod
    ) = add(PaymentMethodUiModel(paymentMethod.label, paymentMethod.value))

    protected fun MutableList<BaseOrderTrackingTypeFactory>.addPaymentDetailListUiModel(
        paymentDetail: List<TokoFoodOrderDetailResponse.TokofoodOrderDetail.Payment.PaymentDetail>
    ) {
        addAll(paymentDetail.map {
            PaymentDetailUiModel(it.label, it.value)
        })
    }

    protected fun MutableList<BaseOrderTrackingTypeFactory>.addPaymentAmountUiModel(
        paymentAmount: TokoFoodOrderDetailResponse.TokofoodOrderDetail.Payment.PaymentAmount
    ) = add(PaymentAmountUiModel(paymentAmount.label, paymentAmount.value))

    protected fun MutableList<BaseOrderTrackingTypeFactory>.addDriverSectionUiModel(
        driverDetails: TokoFoodOrderDetailResponse.TokofoodOrderDetail.DriverDetails,
        phoneNumber: String
    ) {
        add(
            DriverSectionUiModel(
                driverInformationList = mapToDriverInformationList(driverDetails.karma),
                name = driverDetails.name,
                photoUrl = driverDetails.photoUrl,
                phone = phoneNumber,
                licensePlateNumber = driverDetails.licensePlateNumber,
                isCallable = true
            )
        )
    }

    private fun mapToDriverInformationList(
        karma: List<TokoFoodOrderDetailResponse.TokofoodOrderDetail.DriverDetails.Karma>
    ): List<DriverInformationUiModel> {
        return karma.map {
            val icon = when (it.icon) {
                DriverInfoIcon.VACCINE -> IconUnify.VACCINE
                DriverInfoIcon.TERMOMETER -> IconUnify.TEMPERATURE
                DriverInfoIcon.TELEPON -> IconUnify.CALL
                else -> null
            }
            DriverInformationUiModel(iconInformation = icon, informationName = it.message)
        }
    }

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

    protected fun MutableList<BaseOrderTrackingTypeFactory>.addOrderTrackingStatusInfo(
        orderStatus: TokoFoodOrderDetailResponse.TokofoodOrderDetail.OrderStatus
    ) {
        val lottieUrl = getOrderStatusLottieUrl(orderStatus.status)
        add(
            OrderTrackingStatusInfoUiModel(
                stepperStatusList = mapToStepperStatusList(orderStatus.status),
                statusKey = orderStatus.status,
                orderStatusTitle = orderStatus.title,
                orderStatusSubTitle = orderStatus.subtitle,
                lottieUrl = lottieUrl
            )
        )
    }

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

    protected fun MutableList<BaseOrderTrackingTypeFactory>.addFoodItemListUiModel(
        foodList:
        List<TokoFoodOrderDetailResponse.TokofoodOrderDetail.Item>
    ) {
        addAll(foodList.take(Int.ONE).map {
            FoodItemUiModel(
                foodName = it.displayName,
                quantity = it.quantity.toString(),
                priceStr = it.price,
                notes = it.notes.orEmpty(),
                addOnVariantList = mapToAddonVariantUiModel(it)
            )
        })
    }

    private fun mapToAddonVariantUiModel(
        foodItem:
        TokoFoodOrderDetailResponse.TokofoodOrderDetail.Item
    ): List<AddonVariantItemUiModel> {
        return foodItem.variants?.map {
            AddonVariantItemUiModel(displayName = it.displayName, optionName = it.optionName)
        }.orEmpty()
    }

    protected fun MutableList<BaseOrderTrackingTypeFactory>.addOrderDetailToggleCtaUiModel(
        foodList:
        List<TokoFoodOrderDetailResponse.TokofoodOrderDetail.Item>
    ) {
        if (foodList.size > Int.ONE) {
            add(OrderDetailToggleCtaUiModel(false))
        }
    }

    protected fun MutableList<BaseOrderTrackingTypeFactory>.addOrderDetailHeaderUiModel() =
        add(OrderDetailHeaderUiModel())

    protected fun MutableList<BaseOrderTrackingTypeFactory>.addPaymentHeaderUiModel() =
        add(PaymentHeaderUiModel())

    protected fun MutableList<BaseOrderTrackingTypeFactory>.addShippingHeaderUiModel() =
        add(ShippingHeaderUiModel())

    protected fun MutableList<BaseOrderTrackingTypeFactory>.addThickDividerUiModel() =
        add(ThickDividerUiModel())

    protected fun MutableList<BaseOrderTrackingTypeFactory>.addThinDividerUiModel(marginTop: Int) =
        add(ThinDividerUiModel(marginTop))

    protected fun MutableList<BaseOrderTrackingTypeFactory>.addThinDividerMarginUiModel() =
        add(ThinDividerMarginUiModel())

    companion object {
        const val MARGIN_TOP_TWENTY = 20
        const val MARGIN_TOP_EIGHT = 8
    }
}