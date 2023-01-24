package com.tokopedia.homenav.mainnav.view.adapter.typefactory

import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.*

interface OrderListTypeFactory {
    fun type(orderProductRevampModel: OrderProductRevampModel): Int
    fun type(orderPaymentRevampModel: OrderPaymentRevampModel): Int
    fun type(orderPaymentModel: OrderPaymentModel): Int
    fun type(orderProductModel: OrderProductModel): Int
    fun type(otherTransactionModel: OtherTransactionModel): Int
    fun type(orderEmptyModel: OrderEmptyModel): Int
    fun type(otherTransactionRevampModel: OtherTransactionRevampModel): Int
}
