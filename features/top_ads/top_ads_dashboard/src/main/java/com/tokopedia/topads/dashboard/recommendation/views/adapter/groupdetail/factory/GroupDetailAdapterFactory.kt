package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.dashboard.recommendation.data.model.local.*
import com.tokopedia.topads.dashboard.recommendation.data.model.local.groupdetailchips.GroupDetailChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.insighttypechips.InsightTypeChipsUiModel

interface GroupDetailAdapterFactory {
    fun type(insightTypeChipsUiModel: InsightTypeChipsUiModel): Int
    fun type(groupPerformanceWidgetUiModel: GroupPerformanceWidgetUiModel): Int
    fun type(groupDetailChipsUiModel: GroupDetailChipsUiModel): Int
    fun type(groupInsightsUiModel: GroupInsightsUiModel): Int
    fun type(accordianKataKunciUiModel: AccordianKataKunciUiModel): Int
    fun type(accordianKeywordBidUiModel: AccordianKeywordBidUiModel): Int
    fun type(accordianGroupBidUiModel: AccordianGroupBidUiModel): Int
    fun type(accordianNegativeKeywordUiModel: AccordianNegativeKeywordUiModel): Int
    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}
