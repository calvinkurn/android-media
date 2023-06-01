package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsBatchGroupInsightResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianKeywordBidUiModel

class AccordianKeywordBidViewHolder(private val itemView: View) :
    AbstractViewHolder<AccordianKeywordBidUiModel>(itemView) {

    inner class BiayaKataKunciItemsAdapter :
        RecyclerView.Adapter<BiayaKataKunciItemsAdapter.BiayaKataKunciItemsViewHolder>() {
        var biayaKataKunciItemList: List<TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData.ExistingKeywordsBidRecom> = mutableListOf()
        inner class BiayaKataKunciItemsViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val title : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.title)
            private val keywordState: com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.keyword_state_type)
            private val currentCost: com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.current_cost_value)
            private val recommendedValue: com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.recommended_value)
            private val potentialValue: com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.show_potential_value)

            fun bind(element: TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData.ExistingKeywordsBidRecom) {
                title.text = element.keywordTag
                currentCost.text = String.format("Rp%d/klik",element.currentBid)
                recommendedValue.text = String.format("Rp%d/klik",element.suggestionBid)
                potentialValue.text = String.format("+%s kali",element.predictedImpression)
                if(element.keywordType == RecommendationConstants.KEYWORD_TYPE_POSITIVE_PHRASE){
                    keywordState.text = getString(R.string.wide)
                    keywordState.setBackgroundColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN100))
                } else {
                    keywordState.text = getString(R.string.specific)
                    keywordState.setBackgroundColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_BN100))
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BiayaKataKunciItemsViewHolder {
            return BiayaKataKunciItemsViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.topads_insights_accordian_biaya_kata_kunci_item_layout,
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: BiayaKataKunciItemsViewHolder, position: Int) {
            holder.bind(biayaKataKunciItemList[position])
        }

        override fun getItemCount(): Int {
            return biayaKataKunciItemList.count()
        }
    }

    private val adapter by lazy { BiayaKataKunciItemsAdapter() }
    private val biayaKataKunciRv: RecyclerView = itemView.findViewById(R.id.biayaKataKunciRv)

    override fun bind(element: AccordianKeywordBidUiModel?) {
        biayaKataKunciRv.layoutManager =
            LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
        biayaKataKunciRv.adapter = adapter
        element?.existingKeywordsBidRecom?.let {
            adapter.biayaKataKunciItemList = it
        }

        biayaKataKunciRv.addItemDecoration(
            DividerItemDecoration(
                itemView.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    companion object {
        val LAYOUT = R.layout.top_ads_accordian_keyword_bid_layout
    }
}
