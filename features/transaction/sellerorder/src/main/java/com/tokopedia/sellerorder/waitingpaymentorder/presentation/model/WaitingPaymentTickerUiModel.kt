package com.tokopedia.sellerorder.waitingpaymentorder.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.typefactory.WaitingPaymentOrderAdapterTypeFactory

data class WaitingPaymentTickerUiModel(
        val title: String,
        val description: String,
        val type: Int,
        val showCloseIcon: Boolean
): Visitable<WaitingPaymentOrderAdapterTypeFactory> {
    override fun type(typeFactory: WaitingPaymentOrderAdapterTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }
}