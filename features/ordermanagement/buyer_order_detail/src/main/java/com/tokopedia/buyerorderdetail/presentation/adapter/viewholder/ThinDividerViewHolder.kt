package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.ThinDividerUiModel

class ThinDividerViewHolder(itemView: View?): AbstractViewHolder<ThinDividerUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_thin_divider
    }

    override fun bind(element: ThinDividerUiModel?) {
        // noop
    }
}