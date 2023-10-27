package com.tokopedia.home.beranda.presentation.view.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationVisitable

object HomeRecommendationDiffUtil: DiffUtil.ItemCallback<HomeRecommendationVisitable>() {

    override fun getChangePayload(
        oldItem: HomeRecommendationVisitable,
        newItem: HomeRecommendationVisitable
    ): Any? {
        return Pair(oldItem, newItem)
    }

    override fun areItemsTheSame(
        oldItem: HomeRecommendationVisitable,
        newItem: HomeRecommendationVisitable
    ): Boolean {
        return oldItem.getUniqueIdentity() == newItem.getUniqueIdentity()
    }

    override fun areContentsTheSame(
        oldItem: HomeRecommendationVisitable,
        newItem: HomeRecommendationVisitable
    ): Boolean {
        return oldItem.equalsDataModel(newItem)
    }
}
