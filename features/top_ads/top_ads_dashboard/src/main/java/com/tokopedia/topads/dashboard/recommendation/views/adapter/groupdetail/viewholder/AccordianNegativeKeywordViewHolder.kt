package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.topads.common.data.response.KeywordEditInput
import com.tokopedia.topads.common.data.response.TopadsManagePromoGroupProductInput
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.ACTION_CREATE_PARAM
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.CONST_2
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.DEFAULT_PRICE_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.DEFAULT_SUGGESTION_BID
import com.tokopedia.topads.dashboard.recommendation.common.decoration.RecommendationInsightItemDecoration
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsBatchGroupInsightResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianNegativeKeywordUiModel

class AccordianNegativeKeywordViewHolder(
    private val view: View,
    private val onInsightAction: (hasErrors: Boolean) -> Unit
) :
    AbstractViewHolder<AccordianNegativeKeywordUiModel>(view) {
    private var negativeKeywordItemList: List<TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData.NewNegativeKeywordsRecom> = mutableListOf()
    inner class NegativeKeywordItemsAdapter :
        RecyclerView.Adapter<NegativeKeywordItemsAdapter.NegativeKeywordItemsViewHolder>() {

        inner class NegativeKeywordItemsViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {

            private val checkbox : com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify = itemView.findViewById(R.id.checkbox)
            private val title : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.title)
            private val impressionCount : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.impression_count_value)
            private val potentialCount : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.show_potential_value)
            private val keywordStateType : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.keyword_state_type)
            fun bind(element: TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData.NewNegativeKeywordsRecom) {
                bindValues(element)
                setClickListener(element)
                setSelected(element)
            }

            private fun setSelected(element: TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData.NewNegativeKeywordsRecom) {
                checkbox.isChecked = element.isSelected
            }
            private fun setClickListener(element: TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData.NewNegativeKeywordsRecom) {
                checkbox.setOnClickListener {
                    element.isSelected = checkbox.isChecked
                    if(checkbox.isChecked)
                        addItemIntoInput(element)
                    else
                        removeItemFromInput(element)
                    onInsightAction.invoke(false)
                    setSelectAllButtonIndeterminateState()
                }
            }

            private fun bindValues(element: TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData.NewNegativeKeywordsRecom) {
                title.text = element.keywordTag
                impressionCount.text = HtmlCompat.fromHtml(
                    String.format(
                        getString(R.string.topads_dashboard_times_per_day_value),
                        element.predictedImpression
                    ),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                potentialCount.text = HtmlCompat.fromHtml(
                    String.format(
                        getString(R.string.topads_dashboard_price_per_month_template),
                        element.potentialSavings.toString()
                    ),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                if(element.keywordType == RecommendationConstants.KEYWORD_TYPE_POSITIVE_PHRASE){
                    keywordStateType.text = getString(R.string.wide)
                    keywordStateType.setBackgroundColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN100))
                } else {
                    keywordStateType.text = getString(R.string.specific)
                    keywordStateType.setBackgroundColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_BN100))
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
    private var topadsManagePromoGroupProductInput: TopadsManagePromoGroupProductInput? = null

    override fun bind(element: AccordianNegativeKeywordUiModel?) {
        setDefaultInputModel(element)
        setViews(element)
        setClickListener(element)
    }

    private fun setClickListener(element: AccordianNegativeKeywordUiModel?) {
        selectAllCheckbox.setOnClickListener {
            selectAllCheckbox.setIndeterminate(false)
            if(selectAllCheckbox.isChecked) {
                addAllItemsIntoInput(element)
            } else {
                topadsManagePromoGroupProductInput?.keywordOperation = listOf()
            }
            element?.newNegativeKeywordsRecom?.forEach { it.isSelected = selectAllCheckbox.isChecked }
            element?.newNegativeKeywordsRecom?.let {
                adapter.updateList(it)
            }
            onInsightAction.invoke(false)
        }
    }

    private fun setViews(element: AccordianNegativeKeywordUiModel?) {
        negatifKataKunciRv.layoutManager =
            LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
        negatifKataKunciRv.adapter = adapter
        element?.newNegativeKeywordsRecom?.let {
            adapter.updateList(it)
        }
        negatifKataKunciRv.addItemDecoration(
            RecommendationInsightItemDecoration(
                itemView.context,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun setDefaultInputModel(element: AccordianNegativeKeywordUiModel?) {
        topadsManagePromoGroupProductInput = element?.input
        addAllItemsIntoInput(element)
        topadsManagePromoGroupProductInput?.groupInput = null
        element?.newNegativeKeywordsRecom?.forEach { it.isSelected = true }
        onInsightAction.invoke(false)
    }

    private fun addItemIntoInput(element: TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData.NewNegativeKeywordsRecom) {
        val list = topadsManagePromoGroupProductInput?.keywordOperation?.toMutableList()
        list?.add(
            KeywordEditInput(
                ACTION_CREATE_PARAM,
                keyword = KeywordEditInput.Keyword(
                    type = getKeywordType(element.keywordType),
                    status = getKeywordStatus(element.keywordStatus),
                    tag = element.keywordTag,
                    source = element.keywordSource,
                    suggestionPriceBid = DEFAULT_SUGGESTION_BID,
                    price_bid = DEFAULT_PRICE_BID
                )
            )
        )
        topadsManagePromoGroupProductInput?.keywordOperation = list
    }

    private fun removeItemFromInput(element: TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData.NewNegativeKeywordsRecom) {
        topadsManagePromoGroupProductInput?.keywordOperation = topadsManagePromoGroupProductInput?.keywordOperation?.filter { it?.keyword?.tag != element.keywordTag }
    }

    private fun addAllItemsIntoInput(element: AccordianNegativeKeywordUiModel?) {
        val list = mutableListOf<KeywordEditInput>()
        element?.newNegativeKeywordsRecom?.forEach { list.add(
            KeywordEditInput(
                ACTION_CREATE_PARAM,
                keyword = KeywordEditInput.Keyword(
                    type = it.keywordType,
                    status = it.keywordStatus,
                    tag = it.keywordTag,
                    source = it.keywordSource,
                    suggestionPriceBid = DEFAULT_SUGGESTION_BID,
                    price_bid = DEFAULT_PRICE_BID
                )
            )
        ) }
        topadsManagePromoGroupProductInput?.keywordOperation = list
    }

    private fun setSelectAllButtonIndeterminateState(){
        if(topadsManagePromoGroupProductInput?.keywordOperation?.count() == negativeKeywordItemList.count())
            selectAllCheckbox.setIndeterminate(false)
        else
            selectAllCheckbox.setIndeterminate(true)

        selectAllCheckbox.isChecked = topadsManagePromoGroupProductInput?.keywordOperation?.count().toZeroIfNull() > Int.ZERO
    }

    private fun getKeywordType(type: String): String {
        return when(type){
            RecommendationConstants.KEYWORD_TYPE_POSITIVE_EXACT,
            RecommendationConstants.KEYWORD_TYPE_POSITIVE_PHRASE,
            RecommendationConstants.KEYWORD_TYPE_POSITIVE_BROAD,
            RecommendationConstants.KEYWORD_TYPE_NEGATIVE_PHRASE,
            RecommendationConstants.KEYWORD_TYPE_NEGATIVE_EXACT,
            RecommendationConstants.KEYWORD_TYPE_NEGATIVE_BROAD -> type
            else -> {
                if (type.split(' ').firstOrNull()?.length.toZeroIfNull() >= CONST_2)
                    RecommendationConstants.KEYWORD_TYPE_POSITIVE_EXACT
                else
                    RecommendationConstants.KEYWORD_TYPE_POSITIVE_PHRASE
            }
        }
    }

    private fun getKeywordStatus(status : String): String{
        return if(status == RecommendationConstants.KEYWORD_STATUS_ACTIVE || status == RecommendationConstants.KEYWORD_STATUS_INACTIVE || status == RecommendationConstants.KEYWORD_STATUS_DELETED)
            status
        else
            RecommendationConstants.KEYWORD_STATUS_ACTIVE
    }

    companion object {
        val LAYOUT = R.layout.top_ads_accordian_negative_keyword_layout
    }
}
