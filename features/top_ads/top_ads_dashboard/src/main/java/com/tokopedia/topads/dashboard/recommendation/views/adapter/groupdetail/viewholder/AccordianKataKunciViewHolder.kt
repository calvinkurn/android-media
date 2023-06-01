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
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.KEYWORD_TYPE_POSITIVE_PHRASE
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData.NewPositiveKeywordsRecom
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianKataKunciUiModel

class AccordianKataKunciViewHolder(private val itemView: View) :
    AbstractViewHolder<AccordianKataKunciUiModel>(itemView) {

    inner class KataKunciItemsAdapter :
        RecyclerView.Adapter<KataKunciItemsAdapter.KataKunciItemsViewHolder>() {
        var kataKunciItemList: List<NewPositiveKeywordsRecom> = mutableListOf()

        inner class KataKunciItemsViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {

            private val title : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.title)
            private val searchesCount : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.searches_count_value)
            private val potentialCount : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.show_potential_value)
            private val keywordStateType : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.keyword_state_type)
            private val keywordCost : com.tokopedia.unifycomponents.TextFieldUnify2 = itemView.findViewById(R.id.keyword_cost)
            fun bind(element: NewPositiveKeywordsRecom) {
                title.text = element.keywordTag
                searchesCount.text = String.format("%s/bulan",element.totalSearch)
                potentialCount.text = String.format("+%s kali/hari",element.predictedImpression)
                keywordCost.editText.setText(element.suggestionBid.toString())
                if(element.keywordType == KEYWORD_TYPE_POSITIVE_PHRASE){
                    keywordStateType.text = getString(R.string.wide)
                    keywordStateType.setBackgroundColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN100))
                } else {
                    keywordStateType.text = getString(R.string.specific)
                    keywordStateType.setBackgroundColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_BN100))
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KataKunciItemsViewHolder {
            return KataKunciItemsViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.topads_insights_accordian_kata_kunci_item_layout,
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: KataKunciItemsViewHolder, position: Int) {
            holder.bind(kataKunciItemList[position])
        }

        override fun getItemCount(): Int {
            return kataKunciItemList.count()
        }
    }

    private val adapter by lazy { KataKunciItemsAdapter() }
    private val kataKunciRv: RecyclerView = itemView.findViewById(R.id.kataKunciRv)

    override fun bind(element: AccordianKataKunciUiModel?) {
        kataKunciRv.layoutManager =
            LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
        kataKunciRv.adapter = adapter
        element?.newPositiveKeywordsRecom?.let {
            adapter.kataKunciItemList = it
        }
        kataKunciRv.addItemDecoration(
            DividerItemDecoration(
                itemView.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    companion object {
        val LAYOUT = R.layout.top_ads_accordian_kata_kunci_layout
    }
}
