package com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.viewholder.WaitingPaymentOrderProductViewHolder
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrderUiModel

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

class WaitingPaymentOrderProductsAdapterTypeFactory : BaseAdapterTypeFactory() {
    fun type(productUiModel: WaitingPaymentOrderUiModel.ProductUiModel): Int {
        return WaitingPaymentOrderProductViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            WaitingPaymentOrderProductViewHolder.LAYOUT -> WaitingPaymentOrderProductViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}