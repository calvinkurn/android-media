package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.kotlin.extensions.view.orZero

data class OrderStatusUiModel(
        val orderStatusHeaderUiModel: OrderStatusHeaderUiModel,
        val orderStatusInfoUiModel: OrderStatusInfoUiModel,
        val ticker: TickerUiModel
) {
    data class OrderStatusHeaderUiModel(
            val indicatorColor: String,
            val orderId: String,
            val orderStatus: String
    ) : Visitable<BuyerOrderDetailTypeFactory> {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }
    }

    data class OrderStatusInfoUiModel(
            val deadline: DeadlineUiModel,
            val invoice: InvoiceUiModel,
            val purchaseDate: String
    ) : Visitable<BuyerOrderDetailTypeFactory> {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }

        data class InvoiceUiModel(
                val invoice: String,
                val url: String
        )

        data class DeadlineUiModel(
                val color: String,
                val label: String,
                val value: String
        )
    }
}