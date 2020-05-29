package com.tokopedia.buyerorder.list.view.adapter.viewholder

import androidx.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.list.view.adapter.viewmodel.EmptyStateOrderListViewModel

class EmptyStateOrderListViewHolder(itemView: View?) : AbstractViewHolder<EmptyStateOrderListViewModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.empty_state_normal
    }

    override fun bind(element: EmptyStateOrderListViewModel?) {
    }

}