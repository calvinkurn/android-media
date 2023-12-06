package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofLoadingUiModel

class PofLoadingViewHolder(
    view: View
) : AbstractViewHolder<PofLoadingUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_pof_loading
    }

    override fun bind(element: PofLoadingUiModel) {
        // noop
    }

    override fun bind(element: PofLoadingUiModel, payloads: MutableList<Any>) {
        bind(element)
    }
}
