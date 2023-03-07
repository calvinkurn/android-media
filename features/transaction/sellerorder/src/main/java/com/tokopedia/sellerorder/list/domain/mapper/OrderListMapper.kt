package com.tokopedia.sellerorder.list.domain.mapper

import com.tokopedia.kotlin.extensions.view.asCamelCase
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerorder.list.domain.model.SomListOrderListResponse
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderUiModel
import javax.inject.Inject

class OrderListMapper @Inject constructor() {
    fun mapResponseToUiModel(
        orderList: List<SomListOrderListResponse.Data.OrderList.Order>,
        keyword: String
    ): List<SomListOrderUiModel> {
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
                orderProduct = if (it.haveProductBundle) {
                    val bundleDetail = it.bundleDetail
                    val bundleProducts: List<SomListOrderUiModel.OrderProduct> =
                        bundleDetail?.bundle?.map { bundle ->
                            mapProductList(bundle.orderDetail)
                        }?.flatten().orEmpty()
                    val nonBundleProducts: List<SomListOrderUiModel.OrderProduct> =
                        mapProductList(bundleDetail?.nonBundle.orEmpty())
                    listOf(bundleProducts, nonBundleProducts).flatten()
                } else {
                    mapProductList(it.orderProduct)
                },
                productCount = if (it.haveProductBundle) {
                    it.bundleDetail?.totalProduct.orZero()
                } else {
                    it.orderProduct.distinctBy { it.productId }.size
                },
                orderResi = it.orderResi,
                orderStatusId = it.orderStatusId.takeIf { it.isNotBlank() }?.toInt().orZero(),
                status = it.status,
                statusColor = it.statusColor,
                statusIndicatorColor = it.statusIndicatorColor,
                destinationProvince = it.destinationProvince,
                courierName = it.courierName.replace("Kurir", "", false)
                    .replace(Regex("\\s{2,}"), " "),
                courierProductName = it.courierProductName,
                preOrderType = it.preOrderType,
                buyerName = it.buyerName.capitalize(),
                tickerInfo = it.tickerInfo,
                buttons = mapButtons(it.buttons),
                searchParam = keyword,
                orderPlusData = mapOrderPlusData(it.plusData)
            )
        }
    }

    private fun mapProductList(
        orderProduct: List<SomListOrderListResponse.Data.OrderList.Order.Product>
    ): List<SomListOrderUiModel.OrderProduct> {
        return orderProduct.map {
            SomListOrderUiModel.OrderProduct(
                productId = it.productId,
                productName = it.productName.asCamelCase(),
                picture = it.picture,
                quantity = it.productQty
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

    private fun mapOrderPlusData(plusData: SomListOrderListResponse.Data.OrderList.Order.PlusData?): SomListOrderUiModel.OrderPlusData? {
        return plusData?.let { SomListOrderUiModel.OrderPlusData(logoUrl = it.logoUrl) }
    }
}