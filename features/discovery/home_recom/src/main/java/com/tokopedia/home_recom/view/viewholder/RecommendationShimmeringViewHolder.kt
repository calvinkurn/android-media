package com.tokopedia.home_recom.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_recom.R

/**
 * Created by lukas on 21/05/2019
 *
 * A class for holder view shimmering loading
 */
class RecommendationShimmeringViewHolder(view: View) : AbstractViewHolder<LoadingModel>(view){

    override fun bind(element: LoadingModel) {
    }

    companion object{
        val LAYOUT = R.layout.item_recommendation_loading
    }
}