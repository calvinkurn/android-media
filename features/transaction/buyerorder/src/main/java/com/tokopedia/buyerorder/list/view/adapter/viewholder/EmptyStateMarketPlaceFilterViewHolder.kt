package com.tokopedia.buyerorder.list.view.adapter.viewholder

import androidx.annotation.LayoutRes
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.transaction.R
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel.EmptyStateMarketPlaceFilterViewModel

class EmptyStateMarketPlaceFilterViewHolder(itemView: View?, val filterListener: ActionListener) : AbstractViewHolder<EmptyStateMarketPlaceFilterViewModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.order_list_empty_state_marketplace_filter

    }

    private val tryAgain = itemView?.findViewById<LinearLayout>(R.id.filterDate)
    private val heading = itemView?.findViewById<TextView>(R.id.empty_state_marketplace_heading)
    private val subText = itemView?.findViewById<TextView>(R.id.empty_state_marketplace_sub_text)

    override fun bind(element: EmptyStateMarketPlaceFilterViewModel?) {
        heading?.text = getString(R.string.tkpdtransaction_lets_hunt_fav_stuff)
        subText?.text = getString(R.string.tkpdtransaction_buy_dream_items)

        tryAgain?.setOnClickListener {
            filterListener.filterClicked()
        }
    }

    interface ActionListener{
        fun filterClicked()
    }
}