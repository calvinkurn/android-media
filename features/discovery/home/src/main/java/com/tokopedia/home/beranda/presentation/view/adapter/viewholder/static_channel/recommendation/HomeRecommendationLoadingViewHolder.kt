package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationLoading
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener

class HomeRecommendationLoadingViewHolder(view: View) : SmartAbstractViewHolder<HomeRecommendationLoading>(view){
    companion object{
        val LAYOUT = R.layout.item_home_recommendation_loading_grid_layout
    }

    override fun bind(element: HomeRecommendationLoading, listener: SmartListener) {}
}
