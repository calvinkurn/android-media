package com.tokopedia.home.beranda.presentation.view.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.recommendation_widget_common.widget.foryou.ForYouRecommendationVisitable

class HomeRecommendationDiffUtil : DiffUtil.ItemCallback<ForYouRecommendationVisitable>() {
    override fun areItemsTheSame(
        oldItem: ForYouRecommendationVisitable,
        newItem: ForYouRecommendationVisitable
    ): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(
        oldItem: ForYouRecommendationVisitable,
        newItem: ForYouRecommendationVisitable
    ): Boolean {
        return oldItem.areContentsTheSame(newItem)
    }

    override fun getChangePayload(
        oldItem: ForYouRecommendationVisitable,
        newItem: ForYouRecommendationVisitable
    ): Any? {
        return Pair(oldItem, newItem)
    }
}
