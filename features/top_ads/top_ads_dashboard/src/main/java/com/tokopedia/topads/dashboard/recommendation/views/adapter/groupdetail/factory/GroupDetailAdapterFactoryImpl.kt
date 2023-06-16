package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianKataKunciUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianGroupBidUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianNegativeKeywordUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupDetailEmptyStateUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupDetailInsightListUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianDailyBudgetUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AdGroupUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.InsightListUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianKeywordBidUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupInsightsUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupPerformanceWidgetUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.groupdetailchips.GroupDetailChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.insighttypechips.InsightTypeChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder.InsightTypeChipsViewHolder
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder.PerformanceWidgetViewHolder
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder.GroupDetailChipsViewHolder
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder.GroupInsightsViewHolder
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder.AccordianKataKunciViewHolder
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder.AccordianGroupBidViewHolder
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder.AccordianKeywordBidViewHolder
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder.AccordianNegativeKeywordViewHolder
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder.GroupDetailInsightListViewHolder
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder.GroupDetailEmptyStateViewHolder
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder.AccordianDailyBudgetViewHolder

class GroupDetailAdapterFactoryImpl(
    private val onChipClick: (Int) -> Unit,
    private val onInsightItemClick: (list: ArrayList<AdGroupUiModel>, item: AdGroupUiModel) -> Unit,
    private val onInsightTypeChipClick: ((MutableList<InsightListUiModel>?) -> Unit)?,
    private val onAccordianItemClick: ((element: GroupInsightsUiModel) -> Unit),
    private val onInsightAction:(hasErrors: Boolean) -> Unit
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

    override fun type(accordianDailyBudgetUiModel: AccordianDailyBudgetUiModel): Int {
        return AccordianDailyBudgetViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            InsightTypeChipsViewHolder.LAYOUT -> InsightTypeChipsViewHolder(
                view,
                onInsightTypeChipClick
            )
            PerformanceWidgetViewHolder.LAYOUT -> PerformanceWidgetViewHolder(view)
            GroupDetailChipsViewHolder.LAYOUT -> GroupDetailChipsViewHolder(view, onChipClick)
            GroupInsightsViewHolder.LAYOUT -> GroupInsightsViewHolder(
                view,
                onChipClick,
                onInsightTypeChipClick,
                onAccordianItemClick,
                onInsightAction
            )
            AccordianKataKunciViewHolder.LAYOUT -> AccordianKataKunciViewHolder(
                view,
                onInsightAction
            )
            AccordianKeywordBidViewHolder.LAYOUT -> AccordianKeywordBidViewHolder(
                view,
                onInsightAction
            )
            AccordianGroupBidViewHolder.LAYOUT -> AccordianGroupBidViewHolder(view, onInsightAction)
            AccordianDailyBudgetViewHolder.LAYOUT -> AccordianDailyBudgetViewHolder(
                view,
                onInsightAction
            )
            AccordianNegativeKeywordViewHolder.LAYOUT -> AccordianNegativeKeywordViewHolder(
                view,
                onInsightAction
            )
            GroupDetailInsightListViewHolder.LAYOUT -> GroupDetailInsightListViewHolder(
                view,
                onInsightItemClick
            )
            GroupDetailEmptyStateViewHolder.LAYOUT -> GroupDetailEmptyStateViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }
}
