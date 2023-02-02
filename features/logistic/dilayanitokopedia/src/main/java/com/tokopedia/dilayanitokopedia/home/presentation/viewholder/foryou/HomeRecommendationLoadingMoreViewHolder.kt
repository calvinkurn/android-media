package com.tokopedia.dilayanitokopedia.home.presentation.viewholder.foryou

import android.view.View
import com.tokopedia.dilayanitokopedia.R
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationLoadMore
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener

class HomeRecommendationLoadingMoreViewHolder(view: View) : SmartAbstractViewHolder<HomeRecommendationLoadMore>(view) {
    companion object {
        val LAYOUT = R.layout.item_home_recommendation_loading_layout
    }

    override fun bind(element: HomeRecommendationLoadMore, listener: SmartListener) {}
}
