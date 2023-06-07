package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.accordion.AccordionUnify
import com.tokopedia.topads.common.data.response.TopadsManagePromoGroupProductInput
import com.tokopedia.topads.dashboard.R
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
    private val onAccordianItemClick: (clickedItem: Int) -> Unit,
    private val onInsightAction: (topAdsManagePromoGroupProductInput: TopadsManagePromoGroupProductInput?, type: Int?) -> Unit
) :
    AbstractViewHolder<GroupInsightsUiModel>(view) {

    private val accordionUnify: AccordionUnify = itemView.findViewById(R.id.accordionUnify)

    override fun bind(element: GroupInsightsUiModel) {
        accordionUnify.apply {
            accordionData.clear()
            removeAllViews()
            val accordionUnifyData = AccordionDataUnify(
                title = element.title,
                subtitle = element.subTitle,
                isExpanded = element.isExpanded,
                expandableView = getView(element.expandItemDataModel)
            )
            accordionUnifyData.setContentPadding(8.toPx(), 0.toPx(), 8.toPx(), 16.toPx())
            addGroup(accordionUnifyData)
            onItemClick = { _, isExpanded ->
                element.isExpanded = isExpanded
                onAccordianItemClick.invoke(element.type)
            }
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

    companion object {
        val LAYOUT = R.layout.top_ads_group_insights_item_layout
    }
}
