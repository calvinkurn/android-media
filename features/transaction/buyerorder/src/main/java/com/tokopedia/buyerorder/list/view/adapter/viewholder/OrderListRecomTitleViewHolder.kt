package com.tokopedia.buyerorder.list.view.adapter.viewholder

import androidx.annotation.LayoutRes
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.list.view.adapter.viewmodel.OrderListRecomTitleViewModel

class OrderListRecomTitleViewHolder(itemView: View?) : AbstractViewHolder<OrderListRecomTitleViewModel>(itemView) {
    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.order_list_recom_title_item
    }
    private var title = itemView?.findViewById<TextView>(R.id.title)

    override fun bind(element: OrderListRecomTitleViewModel) {
        title?.text = element.title
    }
}