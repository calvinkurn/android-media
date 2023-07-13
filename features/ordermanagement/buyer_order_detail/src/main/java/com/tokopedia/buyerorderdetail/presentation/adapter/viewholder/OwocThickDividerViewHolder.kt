package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.OwocThickDividerUiModel

class OwocThickDividerViewHolder(itemView: View?): AbstractViewHolder<OwocThickDividerUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_owoc_thick_divider
    }

    override fun bind(element: OwocThickDividerUiModel?) {
        // noop
    }
}
