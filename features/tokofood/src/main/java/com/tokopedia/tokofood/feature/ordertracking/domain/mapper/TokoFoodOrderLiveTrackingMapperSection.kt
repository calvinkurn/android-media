package com.tokopedia.tokofood.feature.ordertracking.domain.mapper

import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderDetailResponse
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.BaseOrderTrackingTypeFactory
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.RestaurantUserAddressUiModel
import javax.inject.Inject

class TokoFoodOrderLiveTrackingMapperSection @Inject constructor() : BaseOrderDetailSectionResult(), ITokoFoodOrderLiveTrackingMapper {

    override fun mapToOrderDetailList(
        tokoFoodOrderDetail:
        TokoFoodOrderDetailResponse.TokofoodOrderDetail
    ): List<BaseOrderTrackingTypeFactory> {
        return mutableListOf<BaseOrderTrackingTypeFactory>().apply {
            addTickerUiModel(tokoFoodOrderDetail.additionalTickerInfo)
            addOrderTrackingStatusInfo(tokoFoodOrderDetail.orderStatus)
            addEstimationUiModel(tokoFoodOrderDetail.eta)
            addDriverSectionUiModel(
                tokoFoodOrderDetail.driverDetails,
                tokoFoodOrderDetail.orderStatus,
                tokoFoodOrderDetail.invoice
            )
            addRestaurantUserAddress(tokoFoodOrderDetail.merchant, tokoFoodOrderDetail.destination)
            addThickDividerUiModel()
            addOrderDetailHeaderUiModel()
            addFoodItemListUiModel(tokoFoodOrderDetail.items)
            addThinDividerUiModel(MARGIN_TOP_TWENTY)
            addOrderDetailToggleCtaUiModel(tokoFoodOrderDetail.items)
            addThickDividerUiModel()
            addInvoiceOrderNumberUiModel(tokoFoodOrderDetail.invoice, tokoFoodOrderDetail.payment.paymentDate)
            addThickDividerUiModel()
            addPaymentHeaderUiModel()
            addPaymentMethodUiModel(tokoFoodOrderDetail.payment.paymentMethod)
            addThinDividerMarginUiModel()
            addPaymentDetailListUiModel(tokoFoodOrderDetail.payment.paymentDetails)
            addThinDividerMarginUiModel()
            addPaymentAmountUiModel(tokoFoodOrderDetail.payment.paymentAmount)
        }
    }

    private fun MutableList<BaseOrderTrackingTypeFactory>.addRestaurantUserAddress(
        merchant: TokoFoodOrderDetailResponse.TokofoodOrderDetail.Merchant,
        destination: TokoFoodOrderDetailResponse.TokofoodOrderDetail.Destination
    ) {
        add(
            RestaurantUserAddressUiModel(
                merchantName = merchant.displayName,
                distanceInKm = merchant.distanceInKm,
                destinationLabel = destination.label,
                destinationPhone = destination.phone,
                destinationAddress = destination.info
            )
        )
    }
}
