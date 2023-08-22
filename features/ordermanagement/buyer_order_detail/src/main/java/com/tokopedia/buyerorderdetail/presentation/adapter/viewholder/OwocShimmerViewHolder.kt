package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.OwocShimmerUiModel

class OwocShimmerViewHolder(
    view: View
) : AbstractViewHolder<OwocShimmerUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_owoc_bottomsheet_shimmer
    }

    override fun bind(element: OwocShimmerUiModel) {
        //no op
    }
}
