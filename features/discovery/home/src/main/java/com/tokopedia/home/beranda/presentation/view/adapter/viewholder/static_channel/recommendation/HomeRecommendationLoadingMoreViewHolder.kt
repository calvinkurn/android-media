package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationLoadMore
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener

class HomeRecommendationLoadingMoreViewHolder(view: View) : SmartAbstractViewHolder<HomeRecommendationLoadMore>(view){
    companion object{
        val LAYOUT = R.layout.item_home_recommendation_loading_layout
    }

    override fun bind(element: HomeRecommendationLoadMore, listener: SmartListener) {}
}
