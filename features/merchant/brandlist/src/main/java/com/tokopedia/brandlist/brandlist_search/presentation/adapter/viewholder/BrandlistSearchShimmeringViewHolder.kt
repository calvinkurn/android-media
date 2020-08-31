package com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchShimmeringUiModel

class BrandlistSearchShimmeringViewHolder(view: View): AbstractViewHolder<BrandlistSearchShimmeringUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_search_shimmer
    }

    override fun bind(element: BrandlistSearchShimmeringUiModel?) {
        itemView.visibility = View.VISIBLE
    }
}