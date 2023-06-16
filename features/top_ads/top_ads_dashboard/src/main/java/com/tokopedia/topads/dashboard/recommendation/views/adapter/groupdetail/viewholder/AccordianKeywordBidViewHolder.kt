package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.common.data.response.KeywordEditInput
import com.tokopedia.topads.common.data.response.TopadsManagePromoGroupProductInput
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants
import com.tokopedia.topads.dashboard.recommendation.common.decoration.RecommendationInsightItemDecoration
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsBatchGroupInsightResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianKeywordBidUiModel

class AccordianKeywordBidViewHolder(
    private val itemView: View,
    private val onInsightAction: (hasErrors: Boolean) -> Unit
) :
    AbstractViewHolder<AccordianKeywordBidUiModel>(itemView) {

    inner class BiayaKataKunciItemsAdapter :
        RecyclerView.Adapter<BiayaKataKunciItemsAdapter.BiayaKataKunciItemsViewHolder>() {
        var biayaKataKunciItemList: List<TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData.ExistingKeywordsBidRecom> = mutableListOf()
        inner class BiayaKataKunciItemsViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val checkbox : com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify = itemView.findViewById(R.id.checkbox)
            private val title : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.title)
            private val keywordState: com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.keyword_state_type)
            private val currentCost: com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.current_cost_value)
            private val recommendedValue: com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.recommended_value)
            private val potentialValue: com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.show_potential_value)

            fun bind(element: TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData.ExistingKeywordsBidRecom) {
                setViews(element)
                setCheckedChangeListener(element)
            }

            private fun setViews(element: TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData.ExistingKeywordsBidRecom){
                checkbox.isChecked = element.isSelected
                title.text = element.keywordTag
                currentCost.text = HtmlCompat.fromHtml(
                    String.format(
                        getString(R.string.topads_dashboard_price_per_click_template),
                        element.currentBid.toString()
                    ),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                recommendedValue.text = HtmlCompat.fromHtml(
                    String.format(
                        getString(R.string.topads_dashboard_price_per_click_template),
                        element.suggestionBid.toString()
                    ),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                potentialValue.text = HtmlCompat.fromHtml(
                    String.format(
                        getString(R.string.topads_dashboard_increase_times_template),
                        element.predictedImpression
                    ),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                if(element.keywordType == RecommendationConstants.KEYWORD_TYPE_POSITIVE_PHRASE){
                    keywordState.text = getString(R.string.wide)
                    keywordState.setBackgroundColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN100))
                } else {
                    keywordState.text = getString(R.string.specific)
                    keywordState.setBackgroundColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_BN100))
                }
            }

            private fun setCheckedChangeListener(element: TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData.ExistingKeywordsBidRecom){
                checkbox.setOnCheckedChangeListener(null)

                checkbox.setOnCheckedChangeListener { btn, isChecked ->
                    element.isSelected = isChecked
                    if(isChecked){
                        addTopadsManagePromoGroupProductInput(element)
                    } else {
                        topadsManagePromoGroupProductInput?.keywordOperation = topadsManagePromoGroupProductInput?.keywordOperation?.filter { it?.keyword?.id != element.keywordID }
                    }
                    onInsightAction.invoke(false)
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

        fun updateList(list : List<TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData.ExistingKeywordsBidRecom>){
            biayaKataKunciItemList = list
            notifyDataSetChanged()
        }

    }

    private val adapter by lazy { BiayaKataKunciItemsAdapter() }
    private val biayaKataKunciRv: RecyclerView = itemView.findViewById(R.id.biayaKataKunciRv)
    private val selectAllCheckbox: com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify = itemView.findViewById(R.id.selectAllCheckbox)
    private var topadsManagePromoGroupProductInput: TopadsManagePromoGroupProductInput? = null

    override fun bind(element: AccordianKeywordBidUiModel?) {
        updateInputModel(element)
        setViews(element)
        setCheckedChangeListener(element)
    }

    private fun setCheckedChangeListener(element: AccordianKeywordBidUiModel?) {
        selectAllCheckbox.setOnCheckedChangeListener { btn, isChecked ->
            if(isChecked) {
                val list = mutableListOf<KeywordEditInput>()
                element?.existingKeywordsBidRecom?.forEach { list.add(
                    KeywordEditInput(
                        RecommendationConstants.ACTION_EDIT_PARAM,
                        keyword = KeywordEditInput.Keyword(
                            type = it.keywordType,
                            status = it.keywordStatus,
                            tag = it.keywordTag,
                            suggestionPriceBid = it.suggestionBid.toDouble(),
                            price_bid = it.currentBid.toDouble(),
                            source = it.suggestionBidSource,
                            id = it.keywordID
                        )
                    )
                ) }
                topadsManagePromoGroupProductInput?.keywordOperation = list
            } else {
                topadsManagePromoGroupProductInput?.keywordOperation = listOf()
            }
            element?.existingKeywordsBidRecom?.forEach { it.isSelected = isChecked }
            element?.existingKeywordsBidRecom?.let {
                adapter.updateList(it)
            }
            onInsightAction.invoke(false)
        }
    }

    private fun setViews(element: AccordianKeywordBidUiModel?) {
        biayaKataKunciRv.layoutManager =
            LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
        biayaKataKunciRv.adapter = adapter
        element?.existingKeywordsBidRecom?.let {
            adapter.biayaKataKunciItemList = it
        }

        biayaKataKunciRv.addItemDecoration(
            RecommendationInsightItemDecoration(
                itemView.context,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun updateInputModel(element: AccordianKeywordBidUiModel?) {
        topadsManagePromoGroupProductInput = element?.input
    }

    fun addTopadsManagePromoGroupProductInput(element: TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData.ExistingKeywordsBidRecom){
        val list = topadsManagePromoGroupProductInput?.keywordOperation?.toMutableList()
        list?.add(KeywordEditInput(
            RecommendationConstants.ACTION_EDIT_PARAM,
            keyword = KeywordEditInput.Keyword(
                type = element.keywordType,
                status = element.keywordStatus,
                tag = element.keywordTag,
                suggestionPriceBid = element.suggestionBid.toDouble(),
                price_bid = element.currentBid.toDouble(),
                source = element.suggestionBidSource,
                id = element.keywordID
            )
        ))
        topadsManagePromoGroupProductInput?.keywordOperation = list
    }

    companion object {
        val LAYOUT = R.layout.top_ads_accordian_keyword_bid_layout
    }
}
