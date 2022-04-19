package com.tokopedia.homenav.mainnav.view.adapter.typefactory

import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderPaymentModel
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderProductModel
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OtherTransactionModel

interface OrderListTypeFactory {
    fun type(orderProductModel: OrderProductModel): Int
    fun type(orderPaymentModel: OrderPaymentModel): Int
    fun type(otherTransactionModel: OtherTransactionModel): Int
}