package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupPerformanceWidgetUiModel

class PerformanceWidgetViewHolder(itemView: View) :
    AbstractViewHolder<GroupPerformanceWidgetUiModel>(itemView) {

    override fun bind(element: GroupPerformanceWidgetUiModel?) {

    }

    companion object {
        val LAYOUT = R.layout.top_ads_perfomance_widget_item_layout
    }
}
