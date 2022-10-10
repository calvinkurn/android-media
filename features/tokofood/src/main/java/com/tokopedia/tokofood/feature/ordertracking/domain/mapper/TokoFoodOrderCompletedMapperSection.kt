package com.tokopedia.tokofood.feature.ordertracking.domain.mapper

import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderDetailResponse
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.BaseOrderTrackingTypeFactory
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.CompletedStatusInfoUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ShippingDetailUiModel
import javax.inject.Inject

class TokoFoodOrderCompletedMapperSection @Inject constructor() : BaseOrderDetailSectionResult(),
    ITokoFoodOrderCompletedMapper {

    override fun mapToOrderDetailList(
        tokoFoodOrderDetail:
        TokoFoodOrderDetailResponse.TokofoodOrderDetail
    ): List<BaseOrderTrackingTypeFactory> {
        return mutableListOf<BaseOrderTrackingTypeFactory>().apply {
            addCompletedStatusInfoUiModel(tokoFoodOrderDetail.orderStatus.title)
            addTickerUiModel(tokoFoodOrderDetail.additionalTickerInfo)
            addThinDividerMarginUiModel()
            addInvoiceOrderNumberUiModel(
                tokoFoodOrderDetail.invoice,
                tokoFoodOrderDetail.payment.paymentDate
            )
            addThickDividerUiModel()
            addDriverSectionUiModel(
                tokoFoodOrderDetail.driverDetails,
                tokoFoodOrderDetail.orderStatus,
                tokoFoodOrderDetail.invoice
            )
            addShippingHeaderUiModel()
            addShippingDetailUiModel(tokoFoodOrderDetail.merchant, tokoFoodOrderDetail.destination)
            addThickDividerUiModel()
            addOrderDetailHeaderUiModel()
            addFoodItemListUiModel(tokoFoodOrderDetail.items)
            addThinDividerUiModel(MARGIN_TOP_TWENTY)
            addOrderDetailToggleCtaUiModel(tokoFoodOrderDetail.items)
            addThickDividerUiModel()
            addPaymentHeaderUiModel()
            addPaymentMethodUiModel(tokoFoodOrderDetail.payment.paymentMethod)
            addThinDividerMarginUiModel()
            addPaymentDetailListUiModel(tokoFoodOrderDetail.payment.paymentDetails)
            addThinDividerMarginUiModel()
            addPaymentAmountUiModel(tokoFoodOrderDetail.payment.paymentAmount)
        }
    }

    private fun MutableList<BaseOrderTrackingTypeFactory>.addCompletedStatusInfoUiModel(
        orderStatus: String
    ) {
        add(CompletedStatusInfoUiModel(orderStatus))
    }

    private fun MutableList<BaseOrderTrackingTypeFactory>.addShippingDetailUiModel(
        merchant: TokoFoodOrderDetailResponse.TokofoodOrderDetail.Merchant,
        destination: TokoFoodOrderDetailResponse.TokofoodOrderDetail.Destination
    ) {
        add(
            ShippingDetailUiModel(
                merchantName = merchant.displayName,
                destinationName = destination.label,
                destinationPhone = destination.phone,
                destinationAddress = destination.info
            )
        )
    }
}
