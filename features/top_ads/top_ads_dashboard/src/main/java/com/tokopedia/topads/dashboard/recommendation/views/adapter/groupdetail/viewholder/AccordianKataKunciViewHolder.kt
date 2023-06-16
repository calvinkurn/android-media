package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.topads.common.data.response.KeywordEditInput
import com.tokopedia.topads.common.data.response.TopadsManagePromoGroupProductInput
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.ACTION_CREATE_PARAM
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.INSIGHT_MULTIPLIER
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.KEYWORD_STATUS_ACTIVE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.KEYWORD_STATUS_DELETED
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.KEYWORD_STATUS_INACTIVE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.KEYWORD_TYPE_NEGATIVE_BROAD
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.KEYWORD_TYPE_NEGATIVE_EXACT
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.KEYWORD_TYPE_NEGATIVE_PHRASE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.KEYWORD_TYPE_POSITIVE_BROAD
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.KEYWORD_TYPE_POSITIVE_EXACT
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.KEYWORD_TYPE_POSITIVE_PHRASE
import com.tokopedia.topads.dashboard.recommendation.common.decoration.RecommendationInsightItemDecoration
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData.NewPositiveKeywordsRecom
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianKataKunciUiModel

class AccordianKataKunciViewHolder(
    private val itemView: View,
    private val onInsightAction: (hasErrors: Boolean) -> Unit
) :
    AbstractViewHolder<AccordianKataKunciUiModel>(itemView) {

    inner class KataKunciItemsAdapter :
        RecyclerView.Adapter<KataKunciItemsAdapter.KataKunciItemsViewHolder>() {
        var kataKunciItemList: List<NewPositiveKeywordsRecom> = mutableListOf()

        inner class KataKunciItemsViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {

            private val checkbox : com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify = itemView.findViewById(R.id.checkbox)
            private val title : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.title)
            private val searchesCount : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.searches_count_value)
            private val potentialCount : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.show_potential_value)
            private val keywordStateType : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.keyword_state_type)
            private val keywordCost : com.tokopedia.unifycomponents.TextFieldUnify2 = itemView.findViewById(R.id.keyword_cost)
            fun bind(element: NewPositiveKeywordsRecom) {
                bindValues(element)
                setCheckedChangeListener(element)
                setTextChangeListener(element)
                setSelected(element)
            }

            private fun setTextChangeListener(element: NewPositiveKeywordsRecom) {
                keywordCost.editText.addTextChangedListener(object : TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun afterTextChanged(text: Editable?) {
                        val bid = text.toString().toIntOrZero()
                        if(bid < minBid.toZeroIfNull()){
                            keywordCost.isInputError = true
                            keywordCost.setMessage(String.format(getString(R.string.topads_insight_min_bid_error_msg_format), minBid.toZeroIfNull()))
                        } else if(bid > maxBid.toZeroIfNull()){
                            keywordCost.isInputError = true
                            keywordCost.setMessage(String.format(getString(R.string.topads_insight_max_bid_error_msg_format), maxBid.toZeroIfNull()))
                        } else if(bid % INSIGHT_MULTIPLIER != 0){
                            keywordCost.isInputError = true
                            keywordCost.setMessage(getString(R.string.error_bid_not_multiple_50))
                        } else {
                            keywordCost.isInputError = false
                            element.priceBid = text.toString().toIntOrZero()
                            if(text.toString().toIntOrZero() == element.suggestionBid)
                                keywordCost.setMessage(getString(R.string.biaya_optimal))
                            else
                                keywordCost.setMessage(String.format(getString(R.string.topads_insight_recommended_bid_apply),element.suggestionBid))
                        }
                    }
                })
            }

            private fun bindValues(element: NewPositiveKeywordsRecom) {
                title.text = element.keywordTag
                searchesCount.text = HtmlCompat.fromHtml(
                    String.format(
                        getString(R.string.topads_dashboard_times_per_month_template),
                        element.totalSearch
                    ),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                potentialCount.text = HtmlCompat.fromHtml(
                    String.format(
                        getString(R.string.topads_dashboard_times_per_day_value),
                        element.predictedImpression
                    ),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                keywordCost.editText.setText(element.suggestionBid.toString())
                if (element.keywordType == KEYWORD_TYPE_POSITIVE_PHRASE) {
                    keywordStateType.text = getString(R.string.wide)
                    keywordStateType.setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_GN100
                        )
                    )
                } else {
                    keywordStateType.text = getString(R.string.specific)
                    keywordStateType.setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_BN100
                        )
                    )
                }
            }

            private fun setCheckedChangeListener(element: NewPositiveKeywordsRecom){
                checkbox.setOnCheckedChangeListener(null)
                checkbox.setOnCheckedChangeListener { btn, isChecked ->
                    element.isSelected = isChecked
                    if(isChecked){
                        addTopadsManagePromoGroupProductInput(element)
                    } else {
                        topadsManagePromoGroupProductInput?.keywordOperation = topadsManagePromoGroupProductInput?.keywordOperation?.filter { it?.keyword?.tag != element.keywordTag }
                    }
                    onInsightAction.invoke(hasErrors)
                }
            }

            private fun setSelected(element: NewPositiveKeywordsRecom) {
                checkbox.isChecked = element.isSelected
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

        fun updateList(list : List<NewPositiveKeywordsRecom>){
            kataKunciItemList = list
            notifyDataSetChanged()
        }
    }

    private val adapter by lazy { KataKunciItemsAdapter() }
    private val kataKunciRv: RecyclerView = itemView.findViewById(R.id.kataKunciRv)
    private val selectAllCheckbox: com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify = itemView.findViewById(R.id.selectAllCheckbox)
    private var topadsManagePromoGroupProductInput: TopadsManagePromoGroupProductInput? = null
    private var hasErrors: Boolean = false
    private var maxBid: Int? = 0
    private var minBid: Int? = 0

    override fun bind(element: AccordianKataKunciUiModel?) {
        updateKeys(element)
        setViews(element)
        setCheckedChangeListener(element)
    }

    private fun updateKeys(element: AccordianKataKunciUiModel?) {
        topadsManagePromoGroupProductInput = element?.input
        maxBid = element?.maxBid
        minBid = element?.minBid
    }

    private fun setViews(element: AccordianKataKunciUiModel?) {
        kataKunciRv.layoutManager =
            LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
        kataKunciRv.adapter = adapter
        element?.newPositiveKeywordsRecom?.let {
            adapter.updateList(it)
        }
        kataKunciRv.addItemDecoration(
            RecommendationInsightItemDecoration(
                itemView.context,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun setCheckedChangeListener(element: AccordianKataKunciUiModel?){
        selectAllCheckbox.setOnCheckedChangeListener { btn, isChecked ->
            if(isChecked) {
                val list = mutableListOf<KeywordEditInput>()
                element?.newPositiveKeywordsRecom?.forEach {
                    list.add(
                        KeywordEditInput(
                            ACTION_CREATE_PARAM,
                            keyword = KeywordEditInput.Keyword(
                                type = it.keywordType,
                                status = it.keywordStatus,
                                tag = it.keywordTag,
                                suggestionPriceBid = it.suggestionBid.toDouble(),
                                price_bid = if(it.priceBid.isZero()) it.suggestionBid.toDouble() else it.priceBid.toDouble(),
                                source = it.keywordSource
                            )
                        )
                    )
                }
                topadsManagePromoGroupProductInput?.keywordOperation = list
            } else {
                topadsManagePromoGroupProductInput?.keywordOperation = listOf()
            }
            topadsManagePromoGroupProductInput?.groupInput = null
            element?.newPositiveKeywordsRecom?.forEach { it.isSelected = isChecked }
            element?.newPositiveKeywordsRecom?.let {
                adapter.updateList(it)
            }
            onInsightAction.invoke(hasErrors)
        }
    }

    fun addTopadsManagePromoGroupProductInput(element: NewPositiveKeywordsRecom){
        val list = topadsManagePromoGroupProductInput?.keywordOperation?.toMutableList()
        list?.add(KeywordEditInput(
            ACTION_CREATE_PARAM,
            keyword = KeywordEditInput.Keyword(
                type = getKeywordType(element.keywordType),
                status = getKeywordStatus(element.keywordStatus),
                tag = element.keywordTag,
                price_bid = if(element.priceBid.isZero()) element.suggestionBid.toDouble() else element.priceBid.toDouble(),
                suggestionPriceBid = element.suggestionBid.toDouble(),
                source = element.keywordSource
            )
        ))
        topadsManagePromoGroupProductInput?.keywordOperation = list
        topadsManagePromoGroupProductInput?.groupInput = null
    }

    private fun getKeywordType(type: String): String {
        return when(type){
            KEYWORD_TYPE_POSITIVE_EXACT,
            KEYWORD_TYPE_POSITIVE_PHRASE,
            KEYWORD_TYPE_POSITIVE_BROAD,
            KEYWORD_TYPE_NEGATIVE_PHRASE,
            KEYWORD_TYPE_NEGATIVE_EXACT,
            KEYWORD_TYPE_NEGATIVE_BROAD -> type
            else -> {
                if (type.split(' ').get(0).length >= 2)
                    KEYWORD_TYPE_POSITIVE_EXACT
                else
                    KEYWORD_TYPE_POSITIVE_PHRASE
            }
        }
    }

    private fun getKeywordStatus(status : String): String{
        return if(status == KEYWORD_STATUS_ACTIVE || status == KEYWORD_STATUS_INACTIVE || status == KEYWORD_STATUS_DELETED)
                status
            else
                KEYWORD_STATUS_ACTIVE
    }

    companion object {
        val LAYOUT = R.layout.top_ads_accordian_kata_kunci_layout
    }
}
