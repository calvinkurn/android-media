package com.tokopedia.dilayanitokopedia.ui.recommendation.viewholder

import android.view.View
import com.tokopedia.dilayanitokopedia.R
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel.HomeRecommendationLoading
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener

class HomeRecommendationLoadingViewHolder(view: View) : SmartAbstractViewHolder<HomeRecommendationLoading>(view) {
    companion object {
        val LAYOUT = R.layout.item_dt_home_recommendation_for_you_loading_grid_layout
    }

    override fun bind(element: HomeRecommendationLoading, listener: SmartListener) {
        // no-op
    }
}
