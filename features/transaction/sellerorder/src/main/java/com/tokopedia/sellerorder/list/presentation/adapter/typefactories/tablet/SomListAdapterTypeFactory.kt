package com.tokopedia.sellerorder.list.presentation.adapter.typefactories.tablet

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.list.presentation.adapter.viewholders.SomListOrderEmptyViewHolder
import com.tokopedia.sellerorder.list.presentation.adapter.viewholders.SomListOrderViewHolder

class SomListAdapterTypeFactory(
        orderItemListener: SomListOrderViewHolder.SomListOrderItemListener,
        emptyStateListener: SomListOrderEmptyViewHolder.SomListEmptyStateListener
) : com.tokopedia.sellerorder.list.presentation.adapter.typefactories.SomListAdapterTypeFactory(orderItemListener, emptyStateListener) {

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            SomListOrderViewHolder.LAYOUT -> com.tokopedia.sellerorder.list.presentation.adapter.viewholders.tablet.SomListOrderViewHolder(parent, orderItemListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}