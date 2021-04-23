package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.kotlin.extensions.view.orZero

data class OrderStatusUiModel(
    val orderStatusHeaderUiModel: OrderStatusHeaderUiModel,
    val ticker: TickerUiModel,
    val orderStatusInfoUiModel: OrderStatusInfoUiModel
) {
    data class OrderStatusHeaderUiModel(
            val indicatorColor: String,
            val orderStatus: String
    ): Visitable<BuyerOrderDetailTypeFactory> {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }
    }

    data class OrderStatusInfoUiModel(
            val invoice: String,
            val purchaseDate: String,
            val deadline: Long
    ): Visitable<BuyerOrderDetailTypeFactory> {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }
    }
}