package com.tokopedia.tokofood.feature.ordertracking.domain.mapper

import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderDetailResponse
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.BaseOrderTrackingTypeFactory
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.AddonVariantItemUiModel
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

open class BaseOrderDetailSectionResult: OrderDetailSectionCommon() {

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
            if (eta.time.isNotBlank() && eta.label.isNotBlank()) {
                add(mapToEstimationUiModel(eta))
            }
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
        orderStatus: TokoFoodOrderDetailResponse.TokofoodOrderDetail.OrderStatus,
        invoice: TokoFoodOrderDetailResponse.TokofoodOrderDetail.Invoice
    ) {
        val driverSectionUiModel = mapToDriverSectionUiModel(driverDetails, orderStatus, invoice)
        if (driverSectionUiModel != null) {
            add(driverSectionUiModel)
        }
    }

    protected fun MutableList<BaseOrderTrackingTypeFactory>.addFoodItemListUiModel(
        foodList:
        List<TokoFoodOrderDetailResponse.TokofoodOrderDetail.Item>
    ) {
        addAll(foodList.take(Int.ONE).map {
            FoodItemUiModel(
                cartId = it.cartId,
                categoryId = it.categoryId,
                categoryName = it.categoryName,
                itemId = it.itemId,
                foodName = it.displayName,
                quantity = it.quantity,
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
    }
}
