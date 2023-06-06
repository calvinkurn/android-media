package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.common.data.response.KeywordEditInput
import com.tokopedia.topads.common.data.response.TopadsManagePromoGroupProductInput
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.ACTION_EDIT_PARAM
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsAdGroupBidInsightResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsBatchGroupInsightResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianGroupBidUiModel

class AccordianGroupBidViewHolder(
    private val itemView: View,
    private val onInsightAction: (topAdsManagePromoGroupProductInput: TopadsManagePromoGroupProductInput, type: Int) -> Unit
) :
    AbstractViewHolder<AccordianGroupBidUiModel>(itemView) {

    inner class GroupBidItemsAdapter :
        RecyclerView.Adapter<GroupBidItemsAdapter.GroupBidItemsViewHolder>() {
        var groupBidItemList: List<TopAdsAdGroupBidInsightResponse.TopAdsBatchGetAdGroupBidInsightByGroupID.Group> = mutableListOf()

        inner class GroupBidItemsViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {

            private val checkbox : com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify = itemView.findViewById(R.id.checkbox)
            private val title : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.title)
            private val currentCost : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.current_cost_value)
            private val potentialValue : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.potential_value)
            private val groupCost : com.tokopedia.unifycomponents.TextFieldUnify2 = itemView.findViewById(R.id.group_cost)
            fun bind(element: TopAdsAdGroupBidInsightResponse.TopAdsBatchGetAdGroupBidInsightByGroupID.Group) {
                checkbox.setOnCheckedChangeListener(null)
                checkbox.isChecked = element.adGroupBidInsightData.isSelected
                title.text = ""
                currentCost.text = String.format("Rp%s",element.adGroupBidInsightData.currentBidSettings)
                potentialValue.text = String.format("Rp%s",element.adGroupBidInsightData.predictedTotalImpression)
                groupCost.editText.setText(element.adGroupBidInsightData.suggestionBidSettings.toString())

                checkbox.setOnCheckedChangeListener { btn, isChecked ->
                    element.adGroupBidInsightData.isSelected = isChecked
                    if(isChecked){
                        addTopadsManagePromoGroupProductInput(element)
                    } else {
                        topadsManagePromoGroupProductInput.keywordOperation = topadsManagePromoGroupProductInput.keywordOperation?.filter { it?.keyword?.tag != element.adGroupBidInsightData.groupID }
                    }
                    onInsightAction.invoke(topadsManagePromoGroupProductInput,
                        RecommendationConstants.TYPE_GROUP_BID
                    )
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupBidItemsViewHolder {
            return GroupBidItemsViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.topads_insights_accordian_kata_kunci_item_layout,
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: GroupBidItemsViewHolder, position: Int) {
            holder.bind(groupBidItemList[position])
        }

        override fun getItemCount(): Int {
            return groupBidItemList.count()
        }

        fun updateList(list: List<TopAdsAdGroupBidInsightResponse.TopAdsBatchGetAdGroupBidInsightByGroupID.Group>){
            groupBidItemList = list
            notifyDataSetChanged()
        }
    }

    private val adapter by lazy { GroupBidItemsAdapter() }
    private val biayaIklanRv: RecyclerView = itemView.findViewById(R.id.biayaIklanRv)
    private val topadsManagePromoGroupProductInput: TopadsManagePromoGroupProductInput = TopadsManagePromoGroupProductInput()

    override fun bind(element: AccordianGroupBidUiModel?) {
        biayaIklanRv.layoutManager =
            LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
        biayaIklanRv.adapter = adapter
        element?.topAdsBatchGetAdGroupBidInsightByGroupID?.groups?.let {
            adapter.updateList(it)
        }
        biayaIklanRv.addItemDecoration(
            DividerItemDecoration(
                itemView.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    fun addTopadsManagePromoGroupProductInput(element: TopAdsAdGroupBidInsightResponse.TopAdsBatchGetAdGroupBidInsightByGroupID.Group, ){
        val list = topadsManagePromoGroupProductInput.keywordOperation?.toMutableList()
        list?.add(
            KeywordEditInput(
                ACTION_EDIT_PARAM,
            keyword = KeywordEditInput.Keyword()
        )
        )
        topadsManagePromoGroupProductInput.keywordOperation = list
    }
    companion object {
        val LAYOUT = R.layout.top_ads_accordian_group_bid_layout
    }
}
