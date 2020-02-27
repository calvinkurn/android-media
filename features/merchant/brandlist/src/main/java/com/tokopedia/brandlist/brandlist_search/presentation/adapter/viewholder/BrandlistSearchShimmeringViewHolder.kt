package com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchShimmeringViewModel

class BrandlistSearchShimmeringViewHolder(view: View): AbstractViewHolder<BrandlistSearchShimmeringViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_search_shimmer
    }

    override fun bind(element: BrandlistSearchShimmeringViewModel?) {
        itemView.visibility = View.VISIBLE
    }
}