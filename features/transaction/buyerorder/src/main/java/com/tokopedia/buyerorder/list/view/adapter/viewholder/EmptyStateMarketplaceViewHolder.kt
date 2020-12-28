package com.tokopedia.buyerorder.list.view.adapter.viewholder

import androidx.annotation.LayoutRes
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.list.view.adapter.viewmodel.EmptyStateMarketplaceUiModel
import com.tokopedia.kotlin.extensions.view.hide

class EmptyStateMarketplaceViewHolder(itemView: View?) : AbstractViewHolder<EmptyStateMarketplaceUiModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.order_list_empty_state_marketplace
    }
    private val tryAgain = itemView?.findViewById<TextView>(R.id.tryAgain)
    private val heading = itemView?.findViewById<TextView>(R.id.empty_state_marketplace_heading)
    private val subText = itemView?.findViewById<TextView>(R.id.empty_state_marketplace_sub_text)

    override fun bind(element: EmptyStateMarketplaceUiModel?) {
        tryAgain?.hide()
        heading?.text = getString(R.string.tkpdtransaction_lets_hunt_fav_stuff)
        subText?.text = getString(R.string.tkpdtransaction_buy_dream_items)
    }
}