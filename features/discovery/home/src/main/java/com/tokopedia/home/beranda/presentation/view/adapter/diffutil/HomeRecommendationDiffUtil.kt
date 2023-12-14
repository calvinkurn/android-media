package com.tokopedia.home.beranda.presentation.view.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.BaseHomeRecommendationVisitable

class HomeRecommendationDiffUtil : DiffUtil.ItemCallback<BaseHomeRecommendationVisitable>() {
    override fun areItemsTheSame(
        oldItem: BaseHomeRecommendationVisitable,
        newItem: BaseHomeRecommendationVisitable
    ): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(
        oldItem: BaseHomeRecommendationVisitable,
        newItem: BaseHomeRecommendationVisitable
    ): Boolean {
        return oldItem.areContentsTheSame(newItem)
    }

    override fun getChangePayload(
        oldItem: BaseHomeRecommendationVisitable,
        newItem: BaseHomeRecommendationVisitable
    ): Any? {
        return Pair(oldItem, newItem)
    }
}
