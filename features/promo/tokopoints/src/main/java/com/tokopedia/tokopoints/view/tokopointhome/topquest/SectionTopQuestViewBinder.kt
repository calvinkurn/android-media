package com.tokopedia.tokopoints.view.tokopointhome.topquest

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.homeresponse.RewardsRecommendation
import com.tokopedia.tokopoints.view.tokopointhome.RewardsRecomListener
import com.tokopedia.tokopoints.view.tokopointhome.SectionItemViewBinder

class SectionTopQuestViewBinder(val recommList: RewardsRecommendation, val listener: RewardsRecomListener)
    : SectionItemViewBinder<RewardsRecommendation, SectionTopQuestVH>(
        RewardsRecommendation::class.java) {

    override fun createViewHolder(parent: ViewGroup): SectionTopQuestVH {
        return SectionTopQuestVH(
                LayoutInflater.from(parent.context).inflate(getSectionItemType(), parent, false ), listener)
    }

    override fun bindViewHolder(model:RewardsRecommendation, viewHolder: SectionTopQuestVH) {
        viewHolder.bind(model)
    }

    override fun getSectionItemType() = R.layout.tp_topquest_container

}