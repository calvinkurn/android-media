package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.dashboard.recommendation.data.model.local.*
import com.tokopedia.topads.dashboard.recommendation.data.model.local.groupdetailchips.GroupDetailChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.insighttypechips.InsightTypeChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder.*

class GroupDetailAdapterFactoryImpl(
    private val onChipClick: (Int) -> Unit
) :
    BaseAdapterTypeFactory(), GroupDetailAdapterFactory {
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

    override fun type(accordianKataKunciUiModel: AccordianKataKunciUiModel): Int {
        return AccordianKataKunciViewHolder.LAYOUT
    }

    override fun type(accordianKeywordBidUiModel: AccordianKeywordBidUiModel): Int {
        return AccordianKeywordBidViewHolder.LAYOUT
    }

    override fun type(accordianGroupBidUiModel: AccordianGroupBidUiModel): Int {
        return AccordianGroupBidViewHolder.LAYOUT
    }

    override fun type(accordianNegativeKeywordUiModel: AccordianNegativeKeywordUiModel): Int {
        return AccordianNegativeKeywordViewHolder.LAYOUT
    }

    override fun type(groupDetailEmptyStateUiModel: GroupDetailEmptyStateUiModel): Int {
        return GroupDetailEmptyStateViewHolder.LAYOUT
    }

    override fun type(groupDetailInsightListUiModel: GroupDetailInsightListUiModel): Int {
        return GroupDetailInsightListViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            InsightTypeChipsViewHolder.LAYOUT -> InsightTypeChipsViewHolder(view)
            PerformanceWidgetViewHolder.LAYOUT -> PerformanceWidgetViewHolder(view)
            GroupDetailChipsViewHolder.LAYOUT -> GroupDetailChipsViewHolder(view, onChipClick)
            GroupInsightsViewHolder.LAYOUT -> GroupInsightsViewHolder(view, onChipClick)
            AccordianKataKunciViewHolder.LAYOUT -> AccordianKataKunciViewHolder(view)
            AccordianKeywordBidViewHolder.LAYOUT -> AccordianKeywordBidViewHolder(view)
            AccordianGroupBidViewHolder.LAYOUT -> AccordianGroupBidViewHolder(view)
            AccordianNegativeKeywordViewHolder.LAYOUT -> AccordianNegativeKeywordViewHolder(view)
            GroupDetailInsightListViewHolder.LAYOUT -> GroupDetailInsightListViewHolder(view)
            GroupDetailEmptyStateViewHolder.LAYOUT -> GroupDetailEmptyStateViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }
}
