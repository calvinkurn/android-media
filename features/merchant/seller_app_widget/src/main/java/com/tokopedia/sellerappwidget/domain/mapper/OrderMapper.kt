package com.tokopedia.sellerappwidget.domain.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.data.model.GetOrderResponse
import com.tokopedia.sellerappwidget.data.model.OrderFilterSomModel
import com.tokopedia.sellerappwidget.data.model.OrderProductModel
import com.tokopedia.sellerappwidget.view.model.OrderItemUiModel
import com.tokopedia.sellerappwidget.view.model.OrderProductUiModel
import com.tokopedia.sellerappwidget.view.model.OrderUiModel
import com.tokopedia.sellerappwidget.view.model.SellerOrderStatusUiModel

/**
 * Created By @ilhamsuaib on 17/11/20
 */

class OrderMapper {

    fun mapRemoteModelToUiModel(orderResponse: GetOrderResponse): OrderUiModel {
        return OrderUiModel(
                orders = orderResponse.orderList.list.map {
                    OrderItemUiModel(
                            orderId = it.orderId,
                            deadLineText = it.deadLineText,
                            statusId = it.statusId,
                            product = getProductUiModel(it.orderProducts.getOrNull(0)),
                            productCount = it.orderProducts.size
                    )
                },
                sellerOrderStatus = SellerOrderStatusUiModel(
                        newOrder = getOrderAmount(orderResponse.orderFilterSom, Const.OrderStatusStr.NEW_ORDER),
                        readyToShip = getOrderAmount(orderResponse.orderFilterSom, Const.OrderStatusStr.READY_TO_SHIP)
                )
        )
    }

    private fun getProductUiModel(product: OrderProductModel?): OrderProductUiModel? {
        product?.let {
            return OrderProductUiModel(
                    productId = it.productId,
                    productName = it.productName,
                    picture = it.picture
            )
        }
        return null
    }

    private fun getOrderAmount(orderFilterSom: OrderFilterSomModel?, orderStatus: String): Int {
        return orderFilterSom?.statusList
                ?.firstOrNull {
                    it.key == orderStatus
                }
                ?.orderAmount.orZero()
    }
}