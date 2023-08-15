package com.tokopedia.carouselproductcard.paging.loading

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.R

internal class ShimmeringViewHolder(
    itemView: View
): AbstractViewHolder<ShimmeringDataView>(itemView) {

    override fun bind(element: ShimmeringDataView?) { }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.carousel_paging_shimmering
    }
}
