package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationLoading

class HomeRecommendationLoadingViewHolder(view: View) : AbstractViewHolder<HomeRecommendationLoading>(view) {
    companion object {
        val LAYOUT = R.layout.item_home_recommendation_loading_grid_layout
    }

    override fun bind(element: HomeRecommendationLoading?) {
    }
}
