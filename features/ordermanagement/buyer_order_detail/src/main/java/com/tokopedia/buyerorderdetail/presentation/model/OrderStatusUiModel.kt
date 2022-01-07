package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.coachmark.BuyerOrderDetailCoachMarkItemManager
import com.tokopedia.kotlin.extensions.view.orZero

data class OrderStatusUiModel(
        val orderStatusHeaderUiModel: OrderStatusHeaderUiModel,
        val orderStatusInfoUiModel: OrderStatusInfoUiModel,
        val ticker: TickerUiModel
) {
    data class OrderStatusHeaderUiModel(
            val indicatorColor: String,
            val orderId: String,
            val orderStatus: String,
            val orderStatusId: String,
            val preOrder: PreOrderUiModel
    ) : BaseVisitableUiModel {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }

        data class PreOrderUiModel(
                val isPreOrder: Boolean,
                val label: String,
                val value: String
        )

        override fun shouldShow(): Boolean {
            return orderStatus.isNotBlank()
        }

        override fun getCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager? {
            return null
        }
    }

    data class OrderStatusInfoUiModel(
            val deadline: DeadlineUiModel,
            val invoice: InvoiceUiModel,
            val orderId: String,
            val orderStatusId: String,
            val purchaseDate: String
    ) : BaseVisitableUiModel {
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

        override fun shouldShow(): Boolean {
            return invoice.invoice.isNotBlank() || (deadline.label.isNotBlank() && deadline.value.isNotBlank()) || purchaseDate.isNotBlank()
        }

        override fun getCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager? {
            return null
        }
    }
}