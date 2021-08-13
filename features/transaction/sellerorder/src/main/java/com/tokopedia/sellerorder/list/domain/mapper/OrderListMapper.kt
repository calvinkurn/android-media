package com.tokopedia.sellerorder.list.domain.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerorder.list.domain.model.SomListOrderListResponse
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderUiModel
import javax.inject.Inject

class OrderListMapper @Inject constructor() {
    fun mapResponseToUiModel(orderList: List<SomListOrderListResponse.Data.OrderList.Order>, keyword: String): List<SomListOrderUiModel> {
        return orderList.map {
            SomListOrderUiModel(
                    cancelRequest = it.cancelRequest,
                    cancelRequestNote = it.cancelRequestNote,
                    cancelRequestOriginNote = it.cancelRequestOriginNote,
                    cancelRequestTime = it.cancelRequestTime,
                    cancelRequestStatus = it.cancelRequestStatus,
                    deadlineColor = it.deadlineColor,
                    deadlineText = it.deadlineText,
                    orderId = it.orderId,
                    orderProduct = mapProductList(it.orderProduct),
                    orderResi = it.orderResi,
                    orderStatusId = it.orderStatusId.takeIf { it.isNotBlank() }?.toInt().orZero(),
                    status = it.status,
                    statusColor = it.statusColor,
                    statusIndicatorColor = it.statusIndicatorColor,
                    destinationProvince = it.destinationProvince,
                    courierName = it.courierName.replace("Kurir", "", false).replace(Regex("\\s{2,}"), " "),
                    courierProductName = it.courierProductName,
                    preOrderType = it.preOrderType,
                    buyerName = it.buyerName.capitalize(),
                    tickerInfo = it.tickerInfo,
                    buttons = mapButtons(it.buttons),
                    searchParam = keyword
            )
        }
    }

    private fun mapProductList(orderProduct: List<SomListOrderListResponse.Data.OrderList.Order.OrderProduct>): List<SomListOrderUiModel.OrderProduct> {
        return orderProduct.map {
            SomListOrderUiModel.OrderProduct(
                    productId = it.productId,
                    productName = it.productName.capitalize(),
                    picture = it.picture
            )
        }
    }

    private fun mapButtons(buttons: List<SomListOrderListResponse.Data.OrderList.Order.Button>): List<SomListOrderUiModel.Button> {
        return buttons.map {
            SomListOrderUiModel.Button(
                    key = it.key,
                    displayName = it.displayName,
                    type = it.type,
                    url = it.url,
                    popUp = it.popUp
            )
        }
    }
}