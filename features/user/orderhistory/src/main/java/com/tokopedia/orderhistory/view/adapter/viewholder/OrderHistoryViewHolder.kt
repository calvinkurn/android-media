package com.tokopedia.orderhistory.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.orderhistory.data.Product

class OrderHistoryViewHolder(itemView: View?, val listener: Listener) : AbstractViewHolder<Product>(itemView) {

    interface Listener {

    }

    override fun bind(element: Product?) {
        if (element == null) return
    }

    companion object {
//        val LAYOUT = R.layout.item_order_history
    }
}