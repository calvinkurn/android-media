package com.tokopedia.ordermanagement.buyercancellationorder.domain.mapper

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.ordermanagement.buyercancellationorder.data.getcancellationreason.BuyerGetCancellationReasonData
import com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter.uimodel.BuyerCancellationOrderWrapperUiModel
import com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter.uimodel.BuyerCancellationProductUiModel
import javax.inject.Inject

class GetCancellationReasonMapper @Inject constructor() {

    fun mapToBuyerCancellationOrderWrapperUiModel(
        getCancellationReason: BuyerGetCancellationReasonData.Data.GetCancellationReason
    ): BuyerCancellationOrderWrapperUiModel {
        return BuyerCancellationOrderWrapperUiModel(
            getCancellationReason = getCancellationReason,
            groupedOrderTitle = getCancellationReason.groupedOrders.title,
            groupedOrders = mapToBuyerCancellationOrderList(getCancellationReason.groupedOrders.orderDetails),
            groupType = getCancellationReason.groupType,
            tickerInfo = getCancellationReason.groupedOrders.ticker
        )
    }

    private fun mapToBuyerCancellationOrderList(
        groupedOrderDetails:
        List<BuyerGetCancellationReasonData.Data.GetCancellationReason.GroupedOrders.OrderDetail>
    ): List<BuyerCancellationProductUiModel> {
        return groupedOrderDetails.map {
            BuyerCancellationProductUiModel(
                shopName = it.shopName,
                shopIcon = it.shopIcon,
                invoiceNumber = it.invoiceRefNum,
                productName = it.productName,
                productThumbnailUrl = it.productImage,
                productInfo = it.productInfo,
                //todo will updated later
                orderInfo = String.EMPTY
            )
        }
    }
}
