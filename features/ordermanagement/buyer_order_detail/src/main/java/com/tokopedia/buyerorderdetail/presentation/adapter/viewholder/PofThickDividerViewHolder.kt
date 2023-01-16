package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.PofThickDividerUiModel

class PofThickDividerViewHolder(itemView: View?) :
    AbstractViewHolder<PofThickDividerUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_thick_divider
    }

    override fun bind(element: PofThickDividerUiModel?) {
        // noop
    }
}
