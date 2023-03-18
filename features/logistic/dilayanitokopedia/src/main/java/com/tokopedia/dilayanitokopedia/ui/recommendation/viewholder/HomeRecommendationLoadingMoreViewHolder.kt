package com.tokopedia.dilayanitokopedia.ui.recommendation.viewholder

import android.view.View
import com.tokopedia.dilayanitokopedia.R
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel.HomeRecommendationLoadMore
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener

class HomeRecommendationLoadingMoreViewHolder(view: View) : SmartAbstractViewHolder<HomeRecommendationLoadMore>(view) {
    companion object {
        val LAYOUT = R.layout.item_dt_home_recommendation_loading_layout
    }

    override fun bind(element: HomeRecommendationLoadMore, listener: SmartListener) {
        // no-op
    }
}
