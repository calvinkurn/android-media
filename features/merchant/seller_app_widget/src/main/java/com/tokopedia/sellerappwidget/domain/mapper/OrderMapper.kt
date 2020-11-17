package com.tokopedia.sellerappwidget.domain.mapper

import com.tokopedia.sellerappwidget.data.model.OrderModel
import com.tokopedia.sellerappwidget.data.model.OrderProductModel
import com.tokopedia.sellerappwidget.di.AppWidgetScope
import com.tokopedia.sellerappwidget.view.model.OrderProductUiModel
import com.tokopedia.sellerappwidget.view.model.OrderUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 17/11/20
 */

@AppWidgetScope
class OrderMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(orderList: List<OrderModel>): List<OrderUiModel> {
        return orderList.map {
            OrderUiModel(
                    orderId = it.orderId,
                    deadLineText = it.deadLineText,
                    statusId = it.statusId,
                    product = getProductUiModel(it.orderProducts.getOrNull(0)),
                    productCount = it.orderProducts.size
            )
        }
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
}