package com.tokopedia.recommendation_widget_common.infinite.foryou.utils

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.recommendation_widget_common.infinite.foryou.ForYouRecommendationVisitable

class ForYouRecommendationDiffUtil : DiffUtil.ItemCallback<ForYouRecommendationVisitable>() {

    override fun areItemsTheSame(
        oldItem: ForYouRecommendationVisitable,
        newItem: ForYouRecommendationVisitable
    ) = oldItem.areItemsTheSame(newItem)

    override fun areContentsTheSame(
        oldItem: ForYouRecommendationVisitable,
        newItem: ForYouRecommendationVisitable
    ) = oldItem.areContentsTheSame(newItem)

    override fun getChangePayload(
        oldItem: ForYouRecommendationVisitable,
        newItem: ForYouRecommendationVisitable
    ) = Pair(oldItem, newItem)
}

