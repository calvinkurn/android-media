package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R

class PofShimmerViewHolder(view: View) : AbstractViewHolder<LoadingModel>(view) {

    companion object {
        val LAYOUT = R.layout.partial_order_fulfillment_shimmer
    }

    override fun bind(element: LoadingModel?) {
        // no op
    }
}
