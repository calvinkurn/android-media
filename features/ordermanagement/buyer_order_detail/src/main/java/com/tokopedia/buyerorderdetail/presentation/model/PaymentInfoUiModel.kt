package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.kotlin.extensions.view.orZero

data class PaymentInfoUiModel(
        val headerUiModel: PlainHeaderUiModel,
        val paymentGrandTotal: PaymentGrandTotalUiModel,
        val paymentInfoItems: List<PaymentInfoItemUiModel>,
        val paymentMethodInfoItem: PaymentInfoItemUiModel,
        val ticker: TickerUiModel
) {
    data class PaymentInfoItemUiModel(
            val label: String,
            val value: String
    ) : BaseVisitableUiModel {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }

        override fun shouldShow(): Boolean {
            return label.isNotBlank() && value.isNotBlank()
        }
    }

    data class PaymentGrandTotalUiModel(
            val label: String,
            val value: String
    ) : BaseVisitableUiModel {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }

        override fun shouldShow(): Boolean {
            return label.isNotBlank() && value.isNotBlank()
        }
    }
}