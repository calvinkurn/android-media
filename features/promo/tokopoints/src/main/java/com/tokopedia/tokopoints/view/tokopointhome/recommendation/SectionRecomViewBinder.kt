package com.tokopedia.tokopoints.view.tokopointhome.recommendation

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.tokopointhome.RewardsRecomListener
import com.tokopedia.tokopoints.view.tokopointhome.RewardsRecommendation
import com.tokopedia.tokopoints.view.tokopointhome.SectionItemViewBinder

class SectionRecomViewBinder(val recommList: RewardsRecommendation, val listener: RewardsRecomListener)
    : SectionItemViewBinder<RewardsRecommendation, SectionRecomVH>(
        RewardsRecommendation::class.java) {

    override fun createViewHolder(parent: ViewGroup): SectionRecomVH {
        return SectionRecomVH(
                LayoutInflater.from(parent.context).inflate(getSectionItemType(), parent, false ), listener)
    }

    override fun bindViewHolder(model:RewardsRecommendation, viewHolder: SectionRecomVH) {
        viewHolder.bind(model)
    }

    override fun getSectionItemType() = R.layout.tp_layout_recommendation

}