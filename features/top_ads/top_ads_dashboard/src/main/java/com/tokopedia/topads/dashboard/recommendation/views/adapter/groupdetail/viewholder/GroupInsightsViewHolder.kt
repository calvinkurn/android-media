package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupInsightsUiModel

class GroupInsightsViewHolder(itemView:View): AbstractViewHolder<GroupInsightsUiModel>(itemView) {

    override fun bind(element: GroupInsightsUiModel?) {

    }

    companion object {
        val LAYOUT = R.layout.top_ads_group_insights_item_layout
    }
}
