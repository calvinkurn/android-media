package com.tokopedia.tokofood.feature.ordertracking.domain.mapper

import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.tokofood.feature.ordertracking.domain.constants.DriverInfoIcon
import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderDetailResponse
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.BaseOrderTrackingTypeFactory
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.AddonVariantItemUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DriverInformationUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DriverSectionUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.FoodItemUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailHeaderUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailToggleCtaUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.PaymentAmountUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.PaymentDetailUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.PaymentHeaderUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.PaymentMethodUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ShippingHeaderUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ThickDividerUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ThinDividerMarginUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ThinDividerUiModel

abstract class BaseOrderDetailSectionResult: OrderDetailSectionCommon() {

    abstract fun mapToOrderDetailList(tokoFoodOrderDetail: TokoFoodOrderDetailResponse.TokofoodOrderDetail): List<BaseOrderTrackingTypeFactory>

    protected fun MutableList<BaseOrderTrackingTypeFactory>.addTickerUiModel(
        additionalTickerInfo:
        List<TokoFoodOrderDetailResponse.TokofoodOrderDetail.AdditionalTickerInfo>?
    ) {
        if (additionalTickerInfo?.isNotEmpty() == true) {
            add(mapToTickerInfoData(additionalTickerInfo))
        }
    }

    protected fun MutableList<BaseOrderTrackingTypeFactory>.addOrderTrackingStatusInfo(
        orderStatus: TokoFoodOrderDetailResponse.TokofoodOrderDetail.OrderStatus
    ) {
        add(mapToOrderTrackingStatusInfo(orderStatus))
    }

    protected fun MutableList<BaseOrderTrackingTypeFactory>.addEstimationUiModel(
        eta: TokoFoodOrderDetailResponse.TokofoodOrderDetail.Eta?
    ) {
        if (eta != null) {
            add(mapToEstimationUiModel(eta))
        }
    }

    protected fun MutableList<BaseOrderTrackingTypeFactory>.addInvoiceOrderNumberUiModel(
        invoice: TokoFoodOrderDetailResponse.TokofoodOrderDetail.Invoice,
        paymentDate: String
    ) {
        add(mapToInvoiceOrderNumberUiModel(invoice, paymentDate))
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
        driverDetails: TokoFoodOrderDetailResponse.TokofoodOrderDetail.DriverDetails?,
        phoneNumber: String
    ) {
        if (driverDetails != null) {
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

    protected fun MutableList<BaseOrderTrackingTypeFactory>.addThinDividerUiModel(marginTop: Int?) =
        add(ThinDividerUiModel(marginTop))

    protected fun MutableList<BaseOrderTrackingTypeFactory>.addThinDividerMarginUiModel() =
        add(ThinDividerMarginUiModel())

    companion object {
        const val MARGIN_TOP_TWENTY = 20
        const val MARGIN_TOP_EIGHT = 8
    }
}