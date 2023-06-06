package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.topads.common.data.response.KeywordEditInput
import com.tokopedia.topads.common.data.response.TopadsManagePromoGroupProductInput
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.ACTION_CREATE_PARAM
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsBatchGroupInsightResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianNegativeKeywordUiModel

class AccordianNegativeKeywordViewHolder(
    private val view: View,
    private val onInsightAction: (topAdsManagePromoGroupProductInput: TopadsManagePromoGroupProductInput, type: Int) -> Unit
) :
    AbstractViewHolder<AccordianNegativeKeywordUiModel>(view) {
    inner class NegativeKeywordItemsAdapter :
        RecyclerView.Adapter<NegativeKeywordItemsAdapter.NegativeKeywordItemsViewHolder>() {
        var negativeKeywordItemList: List<TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData.NewNegativeKeywordsRecom> = mutableListOf()

        inner class NegativeKeywordItemsViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {

            private val checkbox : com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify = itemView.findViewById(R.id.checkbox)
            private val title : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.title)
            private val impressionCount : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.impression_count_value)
            private val potentialCount : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.show_potential_value)
            private val keywordStateType : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.keyword_state_type)
            fun bind(element: TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData.NewNegativeKeywordsRecom) {
                checkbox.setOnCheckedChangeListener(null)
                checkbox.isChecked = element.isSelected
                title.text = element.keywordTag
                impressionCount.text = String.format("+%s kali/hari",element.predictedImpression)
                potentialCount.text = String.format("Rp%s /bulan",element.potentialSavings)
                if(element.keywordType == RecommendationConstants.KEYWORD_TYPE_POSITIVE_PHRASE){
                    keywordStateType.text = getString(R.string.wide)
                    keywordStateType.setBackgroundColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN100))
                } else {
                    keywordStateType.text = getString(R.string.specific)
                    keywordStateType.setBackgroundColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_BN100))
                }
                checkbox.setOnCheckedChangeListener { btn, isChecked ->
                    element.isSelected = isChecked
                    if(isChecked){
                        addTopadsManagePromoGroupProductInput(element)
                    } else {
                        topadsManagePromoGroupProductInput.keywordOperation = topadsManagePromoGroupProductInput.keywordOperation?.filter { it?.keyword?.tag != element.keywordTag }
                    }
                    onInsightAction.invoke(topadsManagePromoGroupProductInput,
                        RecommendationConstants.TYPE_NEGATIVE_KEYWORD_BID
                    )
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NegativeKeywordItemsViewHolder {
            return NegativeKeywordItemsViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.topads_insights_accordian_kata_kunci_negatif_item_layout,
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: NegativeKeywordItemsViewHolder, position: Int) {
            holder.bind(negativeKeywordItemList[position])
        }

        override fun getItemCount(): Int {
            return negativeKeywordItemList.count()
        }

        fun updateList(list: List<TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData.NewNegativeKeywordsRecom>){
            negativeKeywordItemList = list
            notifyDataSetChanged()
        }
    }

    private val adapter by lazy { NegativeKeywordItemsAdapter() }
    private val negatifKataKunciRv: RecyclerView = itemView.findViewById(R.id.negatifKataKunciRv)
    private val selectAllCheckbox: com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify = itemView.findViewById(R.id.selectAllCheckbox)
    private val topadsManagePromoGroupProductInput: TopadsManagePromoGroupProductInput = TopadsManagePromoGroupProductInput()

    override fun bind(element: AccordianNegativeKeywordUiModel?) {
        negatifKataKunciRv.layoutManager =
            LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
        negatifKataKunciRv.adapter = adapter
        element?.newNegativeKeywordsRecom?.let {
            adapter.updateList(it)
        }
        negatifKataKunciRv.addItemDecoration(
            DividerItemDecoration(
                itemView.context,
                DividerItemDecoration.VERTICAL
            )
        )

        selectAllCheckbox.setOnCheckedChangeListener { btn, isChecked ->
            if(isChecked) {
                val list = mutableListOf<KeywordEditInput>()
                element?.newNegativeKeywordsRecom?.forEach { list.add(
                    KeywordEditInput(
                    ACTION_CREATE_PARAM,
                    keyword = KeywordEditInput.Keyword(
                        type = it.keywordType,
                        status = it.keywordStatus,
                        tag = it.keywordTag,
                        suggestionPriceBid = it.predictedImpression.toDoubleOrZero(),
                        price_bid = it.potentialSavings.toDouble(),
                        source = it.keywordSource
                    )
                )
                ) }
                topadsManagePromoGroupProductInput.keywordOperation = list
            } else {
                topadsManagePromoGroupProductInput.keywordOperation = listOf()
            }
            element?.newNegativeKeywordsRecom?.forEach { it.isSelected = isChecked }
            element?.newNegativeKeywordsRecom?.let {
                adapter.updateList(it)
            }
            onInsightAction.invoke(topadsManagePromoGroupProductInput,
                RecommendationConstants.TYPE_POSITIVE_KEYWORD
            )
        }
    }

    fun addTopadsManagePromoGroupProductInput(element: TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData.NewNegativeKeywordsRecom ){
        val list = topadsManagePromoGroupProductInput.keywordOperation?.toMutableList()
        list?.add(
            KeywordEditInput(
                ACTION_CREATE_PARAM,
            keyword = KeywordEditInput.Keyword(
                type = element.keywordType,
                status = element.keywordStatus,
                tag = element.keywordTag,
                suggestionPriceBid = element.predictedImpression.toDoubleOrZero(),
                price_bid = element.potentialSavings.toDouble(),
                source = element.keywordSource
            )
        )
        )
        topadsManagePromoGroupProductInput.keywordOperation = list
    }

    companion object {
        val LAYOUT = R.layout.top_ads_accordian_negative_keyword_layout
    }
}
