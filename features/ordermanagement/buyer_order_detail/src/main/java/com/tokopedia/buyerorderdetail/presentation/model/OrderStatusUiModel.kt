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
            val orderId: String,
            val indicatorColor: String,
            val orderStatus: String
    ) : Visitable<BuyerOrderDetailTypeFactory> {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }
    }

    data class OrderStatusInfoUiModel(
            val invoice: InvoiceUiModel,
            val purchaseDate: String,
            val deadline: DeadlineUiModel
    ) : Visitable<BuyerOrderDetailTypeFactory> {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }

        data class InvoiceUiModel(
                val invoice: String,
                val url: String
        )

        data class DeadlineUiModel(
                val label: String,
                val value: Long,
                val color: String
        )
    }
}