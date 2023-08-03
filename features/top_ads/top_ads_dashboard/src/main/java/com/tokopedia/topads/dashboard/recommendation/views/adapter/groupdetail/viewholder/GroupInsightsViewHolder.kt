package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.accordion.AccordionUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupDetailDataModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupInsightsUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.InsightListUiModel
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.GroupDetailAdapter
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory.GroupDetailAdapterFactoryImpl
import com.tokopedia.unifycomponents.toPx

class GroupInsightsViewHolder(
    private val view: View,
    private val onChipClick: (Int) -> Unit,
    private val onInsightTypeChipClick: ((MutableList<InsightListUiModel>?) -> Unit)? = null,
    private val onAccordianItemClick: (element: GroupInsightsUiModel) -> Unit,
    private val onInsightAction: (hasErrors: Boolean) -> Unit
) :
    AbstractViewHolder<GroupInsightsUiModel>(view) {

    private val accordionUnify: AccordionUnify = itemView.findViewById(R.id.accordionUnify)

    override fun bind(element: GroupInsightsUiModel) {
        accordionUnify.apply {
            accordionData.clear()
            removeAllViews()
            val accordionUnifyData = getAccordianData(element)
            accordionUnifyData.setContentPadding(8.toPx(), 16.toPx(), 8.toPx(), 16.toPx())
            addGroup(accordionUnifyData)
            onItemClick = { _, isExpanded ->
                element.isExpanded = isExpanded
                onAccordianItemClick.invoke(element)
                removeAllViews()
                addGroup(getAccordianData(element))
            }
        }
    }

    private fun getAccordianData(element: GroupInsightsUiModel): AccordionDataUnify{
        return AccordionDataUnify(
            title = element.title,
            subtitle = if(element.isExpanded) getSubtitle(element, false) else getSubtitle(element, true),
            isExpanded = element.isExpanded,
            expandableView = getView(element.expandItemDataModel),
            icon = getAccordianIcon(element.type)
        )
    }

    private fun getAccordianIcon(type: Int): Drawable? {
        return when (type) {
            RecommendationConstants.TYPE_POSITIVE_KEYWORD ->
                getIconUnifyDrawable(itemView.context, IconUnify.KEYWORD)
            RecommendationConstants.TYPE_KEYWORD_BID ->
                getIconUnifyDrawable(itemView.context, IconUnify.KEYWORD_BUDGET)
            RecommendationConstants.TYPE_GROUP_BID ->
                getIconUnifyDrawable(itemView.context, IconUnify.ADS_BUDGET)
            RecommendationConstants.TYPE_DAILY_BUDGET ->
                getIconUnifyDrawable(itemView.context, IconUnify.SALDO)
            RecommendationConstants.TYPE_NEGATIVE_KEYWORD_BID ->
                getIconUnifyDrawable(itemView.context, IconUnify.KEYWORD_NEGATIVE)
            else -> null
        }
    }

    private fun getView(expandItemDataModel: GroupDetailDataModel?): View {
        val layout =
            View.inflate(view.context, R.layout.top_ads_group_insights_accordian_layout, null)
        val rv: RecyclerView = layout.findViewById(R.id.accordianRecyclerview)
        rv.layoutManager = LinearLayoutManager(view.context)
        val accordianAdapter = GroupDetailAdapter(GroupDetailAdapterFactoryImpl(onChipClick, { _, _ -> },onInsightTypeChipClick, onAccordianItemClick, onInsightAction = onInsightAction))
        rv.adapter = accordianAdapter
        accordianAdapter.submitList(listOf(expandItemDataModel))
        return layout
    }

    private fun getSubtitle(element: GroupInsightsUiModel, isTruncated: Boolean): String {
        val subtitle = when (element.type) {
            RecommendationConstants.TYPE_POSITIVE_KEYWORD -> String.format(
                getString(R.string.topads_insight_kata_kunci_accordian_subtitle),
                element.subTitleValue
            )
            RecommendationConstants.TYPE_KEYWORD_BID -> String.format(
                getString(R.string.topads_insight_biaya_kata_kunci_accordian_subtitle),
                element.subTitleValue
            )
            RecommendationConstants.TYPE_GROUP_BID -> String.format(
                getString(R.string.topads_insight_biaya_iklan_accordian_subtitle),
                element.subTitleValue
            )
            RecommendationConstants.TYPE_DAILY_BUDGET -> String.format(
                getString(R.string.topads_insight_daily_budget_accordian_subtitle),
                element.subTitleValue
            )
            RecommendationConstants.TYPE_NEGATIVE_KEYWORD_BID -> String.format(
                getString(R.string.topads_insight_negative_kata_kunci_accordian_subtitle),
                element.subTitleValue
            )
            else -> String.EMPTY
        }
        return if (isTruncated)
            "${subtitle.substring(0,subtitle.length/3)}..."
        else
            subtitle
    }

    companion object {
        val LAYOUT = R.layout.top_ads_group_insights_item_layout
    }
}
