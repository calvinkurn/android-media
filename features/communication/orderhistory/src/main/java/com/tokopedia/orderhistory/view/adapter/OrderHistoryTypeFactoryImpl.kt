package com.tokopedia.orderhistory.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.orderhistory.data.Product
import com.tokopedia.orderhistory.view.adapter.viewholder.OrderHistoryViewHolder

class OrderHistoryTypeFactoryImpl(
        private val orderHistoryViewHolderListener: OrderHistoryViewHolder.Listener
) : BaseAdapterTypeFactory(), OrderHistoryTypeFactory {

    override fun type(product: Product): Int {
        return OrderHistoryViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            OrderHistoryViewHolder.LAYOUT -> OrderHistoryViewHolder(parent, orderHistoryViewHolderListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}