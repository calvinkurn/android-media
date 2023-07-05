package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianKataKunciUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianKeywordBidUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupInsightsUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupPerformanceWidgetUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianGroupBidUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianNegativeKeywordUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupDetailEmptyStateUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupDetailInsightListUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianDailyBudgetUiModel
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
    fun type(groupDetailEmptyStateUiModel: GroupDetailEmptyStateUiModel): Int
    fun type(groupDetailInsightListUiModel: GroupDetailInsightListUiModel): Int
    fun type(accordianDailyBudgetUiModel: AccordianDailyBudgetUiModel): Int
    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}
