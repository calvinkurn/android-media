package com.tokopedia.tokopoints.view.tokopointhome.topquest

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.quest_widget.listeners.QuestWidgetCallbacks
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.homeresponse.RewardsRecommendation
import com.tokopedia.tokopoints.view.tokopointhome.RewardsRecomListener
import com.tokopedia.tokopoints.view.tokopointhome.SectionItemViewBinder
import com.tokopedia.tokopoints.view.tokopointhome.TokoPointsHomeFragmentNew

class SectionTopQuestViewBinder(
    var questWidgetCallbacks: QuestWidgetCallbacks,
    )
    : SectionItemViewBinder<RewardsRecommendation, SectionTopQuestVH>(
        RewardsRecommendation::class.java) {

    override fun createViewHolder(parent: ViewGroup): SectionTopQuestVH {
        return SectionTopQuestVH(
                LayoutInflater.from(parent.context).inflate(getSectionItemType(), parent, false ), questWidgetCallbacks)
    }

    override fun bindViewHolder(model:RewardsRecommendation, viewHolder: SectionTopQuestVH) {
        viewHolder.bind(model)
    }

    override fun getSectionItemType() = R.layout.tp_topquest_container

}