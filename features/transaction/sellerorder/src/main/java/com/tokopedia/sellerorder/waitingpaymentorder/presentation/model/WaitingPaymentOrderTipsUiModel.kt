package com.tokopedia.sellerorder.waitingpaymentorder.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.typefactory.WaitingPaymentOrderTipsTypeFactory

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

data class WaitingPaymentOrderTipsUiModel(
        val iconUrl: String,
        val description: String
): Visitable<WaitingPaymentOrderTipsTypeFactory> {
    override fun type(typeFactory: WaitingPaymentOrderTipsTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }
}