package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.PofThinDividerUiModel

class PofThinDividerViewHolder(itemView: View?): AbstractViewHolder<PofThinDividerUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_thin_divider
    }

    override fun bind(element: PofThinDividerUiModel?) {
        // noop
    }
}
