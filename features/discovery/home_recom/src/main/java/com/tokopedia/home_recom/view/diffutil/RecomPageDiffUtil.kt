package com.tokopedia.home_recom.view.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.home_recom.model.datamodel.HomeRecommendationDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationCarouselDataModel

/**
 * Created by yfsx on 01/09/21.
 */
class RecomPageDiffUtil : DiffUtil.ItemCallback<HomeRecommendationDataModel>() {

    override fun areItemsTheSame(oldItem: HomeRecommendationDataModel, newItem: HomeRecommendationDataModel): Boolean {
        return oldItem.name() == newItem.name()
    }

    override fun areContentsTheSame(oldItem: HomeRecommendationDataModel, newItem: HomeRecommendationDataModel): Boolean {
        return oldItem.equalsWith(newItem)
    }

    override fun getChangePayload(oldItem: HomeRecommendationDataModel, newItem: HomeRecommendationDataModel): Any? {
        return oldItem.getChangePayload(newItem)
    }
}