package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.dashboard.recommendation.data.model.local.groupdetailchips.GroupDetailChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupInsightsUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupPerformanceWidgetUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.insighttypechips.InsightTypeChipsUiModel

interface GroupDetailAdapterFactory {
    fun type(insightTypeChipsUiModel: InsightTypeChipsUiModel): Int
    fun type(groupPerformanceWidgetUiModel: GroupPerformanceWidgetUiModel): Int
    fun type(groupDetailChipsUiModel: GroupDetailChipsUiModel): Int
    fun type(groupInsightsUiModel: GroupInsightsUiModel): Int
    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}
