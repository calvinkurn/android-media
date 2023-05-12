package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupInsightsUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupPerformanceWidgetUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.groupdetailchips.GroupDetailChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.insighttypechips.InsightTypeChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder.GroupDetailChipsViewHolder
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder.GroupInsightsViewHolder
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder.InsightTypeChipsViewHolder
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder.PerformanceWidgetViewHolder

class GroupDetailAdapterFactoryImpl() : BaseAdapterTypeFactory(), GroupDetailAdapterFactory {
    override fun type(insightTypeChipsUiModel: InsightTypeChipsUiModel): Int {
        return InsightTypeChipsViewHolder.LAYOUT
    }

    override fun type(groupPerformanceWidgetUiModel: GroupPerformanceWidgetUiModel): Int {
        return PerformanceWidgetViewHolder.LAYOUT
    }

    override fun type(groupDetailChipsUiModel: GroupDetailChipsUiModel): Int {
        return GroupDetailChipsViewHolder.LAYOUT
    }

    override fun type(groupInsightsUiModel: GroupInsightsUiModel): Int {
        return GroupInsightsViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            InsightTypeChipsViewHolder.LAYOUT -> InsightTypeChipsViewHolder(view)
            PerformanceWidgetViewHolder.LAYOUT -> PerformanceWidgetViewHolder(view)
            GroupDetailChipsViewHolder.LAYOUT -> GroupDetailChipsViewHolder(view)
            GroupInsightsViewHolder.LAYOUT -> GroupInsightsViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }
}
