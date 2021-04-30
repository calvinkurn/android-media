package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.kotlin.extensions.view.orZero

data class PaymentInfoUiModel(
        val headerUiModel: PlainHeaderUiModel,
        val paymentGrandTotal: PaymentGrandTotalUiModel,
        val paymentInfoItems: List<PaymentInfoItemUiModel>,
        val paymentMethodInfoItem: PaymentInfoItemUiModel,
        val ticker: TickerUiModel
) {
    open class PaymentInfoItemUiModel(
            val label: String,
            val value: String
    ) : Visitable<BuyerOrderDetailTypeFactory> {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }
    }

    class PaymentGrandTotalUiModel(
            label: String,
            value: String
    ) : PaymentInfoItemUiModel(label, value) {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }
    }
}