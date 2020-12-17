package com.tokopedia.seller.search.feature.initialsearch.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.suggestion.view.model.LoadingSearchModel

class ShimmerLoadingViewHolder(view: View): AbstractViewHolder<LoadingSearchModel>(view) {

    companion object {
        val LAYOUT = R.layout.initial_search_shimmer
    }

    override fun bind(element: LoadingSearchModel) {}
}