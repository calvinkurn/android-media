package com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.viewholder.WaitingPaymentOrderTipsViewHolder
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrderTipsUiModel

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

class WaitingPaymentOrderTipsTypeFactory : BaseAdapterTypeFactory() {
    fun type(waitingPaymentOrderTipsUiModel: WaitingPaymentOrderTipsUiModel): Int {
        return WaitingPaymentOrderTipsViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            WaitingPaymentOrderTipsViewHolder.LAYOUT -> WaitingPaymentOrderTipsViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}