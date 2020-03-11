package com.tokopedia.exploreCategory.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.exploreCategory.R
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.ECShimmerVHViewModel

class ECShimmeringViewHolder(itemView: View) : AbstractViewHolder<ECShimmerVHViewModel>(itemView) {
    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.ec_item_shimmer_layout
    }

    override fun bind(element: ECShimmerVHViewModel?) {

    }
}
