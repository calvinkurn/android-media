package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.coachmark.BuyerOrderDetailCoachMarkItemManager
import com.tokopedia.kotlin.extensions.view.orZero

data class PaymentInfoUiModel(
    val headerUiModel: PlainHeaderUiModel,
    val paymentGrandTotal: PaymentGrandTotalUiModel,
    val paymentInfoItems: List<PaymentInfoItemUiModel>,
    val paymentMethodInfoItem: PaymentInfoItemUiModel,
    val pofRefundInfoUiModel: PofRefundInfoUiModel,
    val ticker: TickerUiModel
) {
    data class PaymentInfoItemUiModel(
        val label: String,
        val value: String
    ) : BaseVisitableUiModel {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }

        override fun shouldShow(context: Context?): Boolean {
            return label.isNotBlank() && value.isNotBlank()
        }

        override fun getCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager? {
            return null
        }
    }

    data class PaymentGrandTotalUiModel(
        val label: String,
        val value: String
    ) : BaseVisitableUiModel {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }

        override fun shouldShow(context: Context?): Boolean {
            return label.isNotBlank() && value.isNotBlank()
        }

        override fun getCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager? {
            return null
        }
    }
}
