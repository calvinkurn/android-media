package com.tokopedia.seller.search.feature.initialsearch.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R

class ShimmerLoadingViewHolder(view: View): AbstractViewHolder<LoadingModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.initial_search_shimmer
    }

    override fun bind(element: LoadingModel) {}
}