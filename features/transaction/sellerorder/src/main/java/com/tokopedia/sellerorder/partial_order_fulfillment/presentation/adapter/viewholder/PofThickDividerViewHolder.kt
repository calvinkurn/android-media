package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofThickDividerUiModel

class PofThickDividerViewHolder(
    view: View
) : AbstractViewHolder<PofThickDividerUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_pof_thick_divider
    }

    override fun bind(element: PofThickDividerUiModel?) {
        // noop
    }

    override fun bind(element: PofThickDividerUiModel, payloads: MutableList<Any>) {
        bind(element)
    }
}
